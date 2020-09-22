package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.IngredientList;
import ch.heiafr.pigroup6.passioncuisine.services.IngredientListService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/shoppinglist/{start-date}/{end-date}")
public class IngredientListController {

    private final IngredientListService ingredientListService;

    public IngredientListController(IngredientListService ingredientListService) {
        this.ingredientListService = ingredientListService;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<List<IngredientList>> getAllBetween(
            @PathVariable("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate.compareTo(endDate) > 0) return ResponseEntity.badRequest().build();

        List<IngredientList> ingredientLists = ingredientListService.getAll(startDate, endDate);

        return ResponseEntity.ok(ingredientLists);
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
//    @PreAuthorize("hasAnyAuthority('admin')")
    public ResponseEntity<InputStreamResource> getAllBetweenPDF(
            @PathVariable("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable("end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        if (startDate.compareTo(endDate) > 0) return ResponseEntity.badRequest().build();

        ByteArrayInputStream bis = ingredientListService.generatePDF(startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=registrations.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
