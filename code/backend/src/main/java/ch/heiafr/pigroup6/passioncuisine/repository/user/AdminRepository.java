package ch.heiafr.pigroup6.passioncuisine.repository.user;

import ch.heiafr.pigroup6.passioncuisine.model.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
}
