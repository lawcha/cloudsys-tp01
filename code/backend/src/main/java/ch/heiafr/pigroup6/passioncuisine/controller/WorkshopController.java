package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.*;
import ch.heiafr.pigroup6.passioncuisine.services.WorkshopService;
import ch.heiafr.pigroup6.passioncuisine.validator.WorkshopDraftValidator;
import ch.heiafr.pigroup6.passioncuisine.validator.WorkshopTerminatedValidator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;

    private final WorkshopDraftValidator workshopDraftValidator;
    private final WorkshopTerminatedValidator workshopTerminatedValidator;

    private final Set<String> filteringKeys;
    private final Set<String> orderingKeys;

    public WorkshopController(WorkshopService workshopService,
                              WorkshopDraftValidator workshopDraftValidator,
                              WorkshopTerminatedValidator workshopTerminatedValidator) {
        this.workshopService = workshopService;
        this.workshopDraftValidator = workshopDraftValidator;
        this.workshopTerminatedValidator = workshopTerminatedValidator;

        filteringKeys = new HashSet<>(Arrays.asList("title", "date", "category"));
        orderingKeys = new HashSet<>(Arrays.asList("title", "date", "category"));
    }

    // ================
    // Get
    @GetMapping(path = "", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<Workshop>> getAll() {
        List<Workshop> workshops = workshopService.getAll();
        return ResponseEntity.ok(workshops);
    }

    @GetMapping(path = "/restricted", produces = "application/json")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<WorkshopRestricted>> getAllRestricted() {
        List<WorkshopRestricted> allRestricted = workshopService.getAllRestricted();
        return ResponseEntity.ok(allRestricted);
    }

    @GetMapping(path = "/{title}/{date}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public ResponseEntity<Workshop> get(@PathVariable("title") String title,
                                        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (title == null || title.isEmpty() || date == null) return ResponseEntity.badRequest().build();
        WorkshopId workshopId = new WorkshopId(title, date);

        Workshop workshop = workshopService.get(workshopId);

        if (workshop == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(workshop);
    }

    @GetMapping(path = "/{state}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') or (hasAnyAuthority('client') and #state == 'published')")
    public ResponseEntity<List<Workshop>> getAll(@PathVariable("state") String state) {
        if (state == null) return ResponseEntity.badRequest().build();

        List<Workshop> workshops = workshopService.getAll(WorkshopState.of(state));

        return ResponseEntity.ok(workshops);
    }
    // ================

    // ================
    // Post
    @PostMapping(path = "", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> editWorkshop(@RequestBody Workshop workshop, BindingResult result) {
        if (workshop instanceof WorkshopDraft) {
            WorkshopDraft draft = (WorkshopDraft) workshop;
            workshopDraftValidator.validate(draft, result);
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

            WorkshopDraft added = workshopService.add(draft);

            return added == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(added);
        } else if (workshop instanceof WorkshopTerminated) {
            WorkshopTerminated terminated = (WorkshopTerminated) workshop;
            workshopTerminatedValidator.validate(terminated, result);
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

            WorkshopTerminated added = workshopService.add(terminated);

            return added == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(added);
        }
        return ResponseEntity.badRequest().build();
    }
    // ================

    // ================
    // Put
    @PutMapping(path = "{title}/{date}", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> editWorkshop(@PathVariable("title") String title,
                                          @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                          @RequestBody Workshop workshop,
                                          BindingResult result) {
        if (title == null || date == null || workshop == null) return ResponseEntity.badRequest().build();
        WorkshopId id = new WorkshopId(title, date);

        if (workshop instanceof WorkshopDraft) {
            WorkshopDraft draft = (WorkshopDraft) workshop;
            workshopDraftValidator.validate(draft, result);
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

            WorkshopDraft edited = workshopService.edit(id, draft);

            return edited == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(edited);
        } else if (workshop instanceof WorkshopTerminated) {
            WorkshopTerminated terminated = (WorkshopTerminated) workshop;
            workshopTerminatedValidator.validate(terminated, result);
            if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

            WorkshopTerminated edited = workshopService.edit(id, terminated);

            return edited == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(edited);
        }
        return ResponseEntity.badRequest().build();
    }
    // ================

    // ================
    // Delete
    @DeleteMapping(path = "/{title}/{date}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Workshop> deleteWorkshopDraft(@PathVariable("title") String title,
                                                        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (title == null || date == null) return ResponseEntity.badRequest().build();
        WorkshopId workshopId = new WorkshopId(title, date);

        Workshop deleted = workshopService.delete(workshopId);

        return ResponseEntity.ok(deleted);
    }
    // ================

    // ================
    // Others
    @PostMapping(path = "/transfer/draft", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> transferToDraft(@RequestBody WorkshopDraft workshop,
                                             BindingResult result) {
        workshopDraftValidator.validate(workshop, result);
        if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

        workshopService.transfer(workshop);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/transfer/published", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> transferToPublished(@RequestBody WorkshopTerminated workshop,
                                                 BindingResult result) {
        workshopTerminatedValidator.validate(workshop, result);
        if (result.hasErrors()) return ResponseEntity.badRequest().body(result.getAllErrors());

        workshopService.transfer(workshop);
        return ResponseEntity.ok().build();
    }

    @Scheduled(cron = "0 0 24 * * *")
    public void transferToClosed() {
        workshopService.transferClosed();
    }
    // ================

    // ================
    // Filtering and ordering
    @PostMapping(path = "/{title}/{date}/duplicate", produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<Workshop> duplicateWorkshop(@PathVariable("title") String title,
                                                      @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                      @RequestBody WorkshopId newId) {
        WorkshopId oldId = new WorkshopId(title, date);
        if (oldId.equals(newId)) return ResponseEntity.badRequest().build();

        Workshop duplicated = workshopService.duplicate(oldId, newId);
        return ResponseEntity.ok(duplicated);
    }

    @GetMapping(path = "/filter/{key}/{value}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getAllFiltered(@PathVariable String key, @PathVariable String value) {
        if (!filteringKeys.contains(key)) return ResponseEntity.badRequest().build();

        List<Workshop> all = workshopService.getAll(key, value);
        return ResponseEntity.ok(all);
    }

    @GetMapping(path = "/{state}/filter/{key}/{value}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') or (hasAnyAuthority('client') and #state == 'published')")
    public ResponseEntity<List<Workshop>> getAll(@PathVariable("state") String state,
                                                 @PathVariable("key") String key,
                                                 @PathVariable("value") String value) {
        if (state == null || !filteringKeys.contains(key)) return ResponseEntity.badRequest().build();

        List<Workshop> workshops = workshopService.getAll(WorkshopState.of(state), key, value);

        return ResponseEntity.ok(workshops);
    }

    @GetMapping(path = "/{state}/order/{key}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin') or (hasAnyAuthority('client') and #state == 'published')")
    public ResponseEntity<List<Workshop>> getAll(@PathVariable("state") String state,
                                                 @PathVariable("key") String key) {
        if (state == null || !orderingKeys.contains(key)) return ResponseEntity.badRequest().build();

        List<Workshop> workshops = workshopService.getAll(WorkshopState.of(state), key);

        return ResponseEntity.ok(workshops);
    }

    @GetMapping(path = "/order/{key}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<?> getAllOrdered(@PathVariable String key) {
        if (!orderingKeys.contains(key)) return ResponseEntity.badRequest().build();

        List<Workshop> all = workshopService.getAll(key);
        return ResponseEntity.ok(all);
    }

    @GetMapping(path = "/restricted/order/{key}", produces = "application/json")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<WorkshopRestricted>> getAllRestricted(@PathVariable String key) {
        if (!orderingKeys.contains(key)) return ResponseEntity.badRequest().build();

        List<WorkshopRestricted> allRestricted = workshopService.getAllRestricted(key);
        return ResponseEntity.ok(allRestricted);
    }

    // ================

    @GetMapping(value = "/incoming/export", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<InputStreamResource> getAllSubsPDF() {
        ByteArrayInputStream bis = workshopService.generatePDFUpcoming();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=registrations.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping(value = "/{email}/export", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyAuthority('client')")
    public ResponseEntity<InputStreamResource> getAllSubsPDF(@PathVariable("email") String email) {
        if (email == null || email.isEmpty()) return ResponseEntity.badRequest().build();

        ByteArrayInputStream bis = workshopService.generatePDFClientList(email);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=registrations.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
