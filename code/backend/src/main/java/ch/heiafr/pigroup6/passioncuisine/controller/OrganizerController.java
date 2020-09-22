package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.Organizer;
import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.Workshop;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopId;
import ch.heiafr.pigroup6.passioncuisine.repository.OrganizerRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {
    private final OrganizerRepository organizerRepository;

    public OrganizerController(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public List<Organizer> get() {
        return organizerRepository.findAll();
    }

    @GetMapping(path = "{email}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public ResponseEntity<Organizer> get(@PathVariable("email") String email) {
        if (email == null || email.isEmpty()) return ResponseEntity.badRequest().build();

        Optional<Organizer> organizer = organizerRepository.findById(email);
        return ResponseEntity.of(organizer);
    }
}
