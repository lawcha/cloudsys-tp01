package ch.heiafr.pigroup6.passioncuisine.repository;

import ch.heiafr.pigroup6.passioncuisine.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
