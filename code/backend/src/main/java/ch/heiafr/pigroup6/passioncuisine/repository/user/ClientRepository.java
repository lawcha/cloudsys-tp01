package ch.heiafr.pigroup6.passioncuisine.repository.user;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ClientRepository extends JpaRepository<Client, String> {

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO client (email, password, lastname, firstname, street, phone, npa, city) " +
            "VALUES (:email, crypt(:password, gen_salt('bf')), :lastname, :firstname, :street, :phone, :npa, :city)",
            nativeQuery = true)
    void saveClient(@Param("email") String email,
                    @Param("password") String password,
                    @Param("lastname") String lastname,
                    @Param("firstname") String firstname,
                    @Param("street") String street,
                    @Param("phone") String phone,
                    @Param("npa") short npa,
                    @Param("city") String city);
}
