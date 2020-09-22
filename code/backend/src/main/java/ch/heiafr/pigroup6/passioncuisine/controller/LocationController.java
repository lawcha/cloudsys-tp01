package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import ch.heiafr.pigroup6.passioncuisine.repository.LocationRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationRepository repository;

    public LocationController(LocationRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("permitAll()")
    public List<Location> get() {
        return repository.findAll();
    }
}
