package ch.heiafr.pigroup6.passioncuisine.repository;

import ch.heiafr.pigroup6.passioncuisine.model.location.Location;
import ch.heiafr.pigroup6.passioncuisine.model.location.LocationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, LocationId> {
}
