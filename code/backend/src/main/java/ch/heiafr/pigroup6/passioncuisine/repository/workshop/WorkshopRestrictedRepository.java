package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopId;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRestricted;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkshopRestrictedRepository extends JpaRepository<WorkshopRestricted, WorkshopId> {
}
