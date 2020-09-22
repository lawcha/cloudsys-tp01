package ch.heiafr.pigroup6.passioncuisine.controller;

import ch.heiafr.pigroup6.passioncuisine.model.Category;
import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import ch.heiafr.pigroup6.passioncuisine.repository.CategoryRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAnyAuthority('admin', 'client')")
    public List<Category> get() {
        return repository.findAll();
    }
}
