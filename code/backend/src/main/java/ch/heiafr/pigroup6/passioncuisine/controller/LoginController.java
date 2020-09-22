package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.StatusMessage;
import ch.heiafr.pigroup6.passioncuisine.model.users.Admin;
import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.users.User;
import ch.heiafr.pigroup6.passioncuisine.services.UserService;
import ch.heiafr.pigroup6.passioncuisine.validator.ClientValidator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;

    private final ClientValidator clientValidator;

    public LoginController(UserService userService, ClientValidator clientValidator) {
        this.userService = userService;
        this.clientValidator = clientValidator;
    }

    @PostMapping(value = "/signup", produces = "application/json", consumes = "application/json")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> signUp(@RequestBody Client client, BindingResult result) {
        clientValidator.validate(client, result);
        if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

        userService.saveClient(client);
        return ResponseEntity.status(200).body(new StatusMessage(200, "Sign-up was successful"));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public ResponseEntity<?> logout() {
        return ResponseEntity.status(200).body(new StatusMessage(200, "Logout was successful"));
    }

    // Debug
    @GetMapping("/admins")
    @PreAuthorize("hasAnyAuthority('admin')")
    public List<Admin> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping("/clients")
    @PreAuthorize("hasAnyAuthority('admin')")
    public List<Client> getAllClients() {
        return userService.getAllClients();
    }

    // Private
    private void getHeaderToken() {

    }

    private void areTokensValid() {

    }

    private void addHeaderToken() {

    }
}
