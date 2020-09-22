package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopId;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRegistration;
import ch.heiafr.pigroup6.passioncuisine.services.WorkshopRegistrationService;
import ch.heiafr.pigroup6.passioncuisine.validator.WorkshopRegistrationValidator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workshops/{title}/{date}/subscriptions")
public class WorkshopRegistrationController {

    private final WorkshopRegistrationService registrationService;
    private final WorkshopRegistrationValidator registrationValidator;

    public WorkshopRegistrationController(WorkshopRegistrationService registrationService,
                                          WorkshopRegistrationValidator registrationValidator) {
        this.registrationService = registrationService;
        this.registrationValidator = registrationValidator;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<WorkshopRegistration>> getAllSubs(@PathVariable("title") String title,
                                                                 @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (title == null || title.isEmpty() || date == null) return ResponseEntity.badRequest().build();
        WorkshopId workshopId = new WorkshopId(title, date);

        List<WorkshopRegistration> registrations = registrationService.getAll(workshopId);
        if (registrations == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(registrations);
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<InputStreamResource> getAllSubsPDF(@PathVariable("title") String title,
                                                             @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (title == null || title.isEmpty() || date == null) return ResponseEntity.badRequest().build();
        WorkshopId workshopId = new WorkshopId(title, date);

        ByteArrayInputStream bis = registrationService.generatePDFRegistrationList(workshopId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=registrations.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping(path = "/{email}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('client')")
    public ResponseEntity<?> getSubscription(@PathVariable("title") String title,
                                             @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             @PathVariable("email") String email) {
        if (title == null || title.isEmpty() ||
                date == null ||
                email == null || email.isEmpty()) return ResponseEntity.badRequest().build();
        WorkshopRegistration registration = new WorkshopRegistration(email, title, date);

        Optional<WorkshopRegistration> found = registrationService.get(registration);
        if (!found.isPresent()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(found.get());
    }

    @PostMapping(path = "/{email}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('client')")
    public ResponseEntity<?> addSubscription(@PathVariable("title") String title,
                                             @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             @PathVariable("email") String email) {
        if (title == null || title.isEmpty() ||
                date == null ||
                email == null || email.isEmpty()) return ResponseEntity.badRequest().build();
        WorkshopRegistration registration = new WorkshopRegistration(email, title, date);

        WorkshopRegistration saved = registrationService.save(registration);
        if (saved == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(saved);
    }


    @DeleteMapping(path = "/{email}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('client')")
    public ResponseEntity<?> deleteSubscription(@PathVariable("title") String title,
                                                @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                @PathVariable("email") String email) {
        if (title == null || title.isEmpty() || date == null || email == null || email.isEmpty())
            return ResponseEntity.badRequest().build();

        WorkshopRegistration registration = new WorkshopRegistration(email, title, date);
        registrationService.delete(registration);
        return ResponseEntity.ok().build();
    }
}
