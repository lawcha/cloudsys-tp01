package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopDraft;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopId;
import org.postgresql.util.PGbytea;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedStoredProcedureQuery;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface WorkshopDraftRepository extends JpaRepository<WorkshopDraft, WorkshopId>, JpaSpecificationExecutor<WorkshopDraft> {

    @Transactional
    @Modifying
    @Procedure("duplicatedraft")
    void duplicate(String oldTitle, LocalDate oldDate, String newTitle, LocalDate newDate);

    @Transactional
    @Modifying
    @Procedure("editworkshopdraft")
    void edit(String idTitle,
              LocalDate idDate,
              String title,
              LocalDate date,
              String dateInscription,
              String street,
              String description,
              String ingredients,
              short max,
              short min,
              String cat,
              String organizer,
              short npa,
              String city,
              String image);

    @Transactional
    @Modifying
    @Procedure("drafttopublished")
    void toPublished(String title,
                     LocalDate date,
                     String dateInscription,
                     String street,
                     String description,
                     String ingredients,
                     short max,
                     short min,
                     String cat,
                     String organizer,
                     short npa,
                     String city,
                     String image);
}
