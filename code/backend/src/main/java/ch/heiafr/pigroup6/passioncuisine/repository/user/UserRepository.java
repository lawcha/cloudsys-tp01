package ch.heiafr.pigroup6.passioncuisine.repository.user;

import ch.heiafr.pigroup6.passioncuisine.model.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, String> {

    @Query(value = "select email from client where email = ?1 and password = crypt(?2, password) " +
            "union " +
            "select email from admin where email = ?1 and password = crypt(?2, password)", nativeQuery = true)
    String login(String email, String password);
}
