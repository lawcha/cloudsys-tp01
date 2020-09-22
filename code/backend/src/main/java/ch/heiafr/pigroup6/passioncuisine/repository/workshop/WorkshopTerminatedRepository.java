package ch.heiafr.pigroup6.passioncuisine.repository.workshop;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkshopTerminatedRepository extends JpaRepository<WorkshopTerminated, WorkshopId>, JpaSpecificationExecutor<WorkshopTerminated> {
    List<WorkshopTerminated> findAllByDateIsBetween(LocalDate startDate, LocalDate endDate);

    List<WorkshopTerminated> findAllByStateIs(WorkshopState state);

    List<WorkshopTerminated> findAllByStateIs(WorkshopState state, Sort sort);

    @Transactional
    @Modifying
    @Procedure("duplicate")
    void duplicate(String oldTitle, LocalDate oldDate, String newTitle, LocalDate newDate);

    @Transactional
    @Modifying
    @Procedure("editworkshop")
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
    @Procedure("publishedtodraft")
    void toDraft(String title,
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
    @Procedure("publishedtoclosed")
    void toClosed();
}
