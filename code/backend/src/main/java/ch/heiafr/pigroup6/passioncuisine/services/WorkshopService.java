package ch.heiafr.pigroup6.passioncuisine.services;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.*;
import ch.heiafr.pigroup6.passioncuisine.repository.workshop.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.postgresql.util.PGbytea;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class WorkshopService {
    private final EntityManager em;

    private final WorkshopDraftRepository draftRepository;
    private final WorkshopTerminatedRepository terminatedRepository;
    private final WorkshopRestrictedRepository restrictedRepository;

    private final Specification<WorkshopTerminated> specificationFetchUpcoming;

    public WorkshopService(EntityManager em, WorkshopDraftRepository draftRepository,
                           WorkshopTerminatedRepository terminatedRepository,
                           WorkshopRestrictedRepository restrictedRepository) {
        this.em = em;
        this.draftRepository = draftRepository;
        this.terminatedRepository = terminatedRepository;
        this.restrictedRepository = restrictedRepository;

        specificationFetchUpcoming = (root, q, b) -> {
            Subquery<Long> subsCountQuery = q.subquery(Long.class);
            Root<WorkshopRegistration> subsRoot = subsCountQuery.from(WorkshopRegistration.class);

            subsCountQuery.select(b.count(subsRoot));
            subsCountQuery.where(
                    b.equal(subsRoot.get("titleWorkshop"), root.get("title")),
                    b.equal(subsRoot.get("dateWorkshop"), root.get("date"))
            );

            return b.createQuery(WorkshopTerminated.class)
                    .where(
                            b.equal(root.get("state"), WorkshopState.published),
                            b.lessThan(root.get("inscriptionLimit"), b.currentTimestamp()),
                            b.greaterThanOrEqualTo(subsCountQuery, root.get("minParticipants"))
                    )
                    .getRestriction();
        };
    }

    // ================
    // Get all
    public List<Workshop> getAll(WorkshopState state) {
        List<Workshop> workshops = new ArrayList<>();

        if (state == WorkshopState.draft) {
            workshops.addAll(draftRepository.findAll());
        } else {
            workshops.addAll(terminatedRepository.findAllByStateIs(state));
        }

        return workshops;
    }

    public List<Workshop> getAll() {
        List<Workshop> workshops = new ArrayList<>();
        workshops.addAll(draftRepository.findAll());
        workshops.addAll(terminatedRepository.findAll());
        return workshops;
    }

    public List<WorkshopRestricted> getAllRestricted() {
        return restrictedRepository.findAll();
    }
    // ================

    // ================
    // Get single
    public Workshop get(WorkshopId id) {
        Optional<WorkshopTerminated> workshopTerminated = terminatedRepository.findById(id);
        if (workshopTerminated.isPresent()) return workshopTerminated.get();

        Optional<WorkshopDraft> workshopDraft = draftRepository.findById(id);
        if (workshopDraft.isPresent()) return workshopDraft.get();

        return null;
    }
    // ================

    // ================
    // Add
    public WorkshopDraft add(WorkshopDraft workshop) {
        return draftRepository.save(workshop);
    }

    public WorkshopTerminated add(WorkshopTerminated workshop) {
        return terminatedRepository.save(workshop);
    }
    // ================

    // ================
    // Add
    public WorkshopDraft edit(WorkshopId id, WorkshopDraft workshop) {
        draftRepository.edit(id.getTitle(),
                id.getDate(),
                workshop.getTitle(),
                workshop.getDate(),
                workshop.getInscriptionLimit().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                workshop.getStreet(),
                workshop.getDescription(),
                workshop.getIngredients(),
                workshop.getMaxParticipants(),
                workshop.getMinParticipants(),
                workshop.getCategory(),
                workshop.getEmailOrganizer(),
                workshop.getLocation().getNpa(),
                workshop.getLocation().getCity(),
                workshop.getImage());

        return (WorkshopDraft) get(workshop.getId());
    }

    public WorkshopTerminated edit(WorkshopId id, WorkshopTerminated workshop) {
        terminatedRepository.edit(id.getTitle(),
                id.getDate(),
                workshop.getTitle(),
                workshop.getDate(),
                workshop.getInscriptionLimit().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                workshop.getStreet(),
                workshop.getDescription(),
                workshop.getIngredients(),
                workshop.getMaxParticipants(),
                workshop.getMinParticipants(),
                workshop.getCategory(),
                workshop.getEmailOrganizer(),
                workshop.getLocation().getNpa(),
                workshop.getLocation().getCity(),
                workshop.getImage());

        return (WorkshopTerminated) get(workshop.getId());
    }
    // ================

    // ================
    // Delete
    public Workshop delete(WorkshopId id) {
        Optional<WorkshopTerminated> workshopTerminated = terminatedRepository.findById(id);
        if (workshopTerminated.isPresent()) {
            terminatedRepository.deleteById(id);
            return workshopTerminated.get();
        }

        Optional<WorkshopDraft> workshopDraft = draftRepository.findById(id);
        if (workshopDraft.isPresent()) {
            draftRepository.deleteById(id);
            return workshopDraft.get();
        }

        return null;
    }
    // ================

    // ================
    // Transfer
    public void transfer(WorkshopDraft workshop) {
        terminatedRepository.toDraft(workshop.getTitle(),
                workshop.getDate(),
                workshop.getInscriptionLimit().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                workshop.getStreet(),
                workshop.getDescription(),
                workshop.getIngredients(),
                workshop.getMaxParticipants(),
                workshop.getMinParticipants(),
                workshop.getCategory(),
                workshop.getEmailOrganizer(),
                workshop.getLocation().getNpa(),
                workshop.getLocation().getCity(),
                workshop.getImage());
    }

    public void transfer(WorkshopTerminated workshop) {
        draftRepository.toPublished(workshop.getTitle(),
                workshop.getDate(),
                workshop.getInscriptionLimit().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")),
                workshop.getStreet(),
                workshop.getDescription(),
                workshop.getIngredients(),
                workshop.getMaxParticipants(),
                workshop.getMinParticipants(),
                workshop.getCategory(),
                workshop.getEmailOrganizer(),
                workshop.getLocation().getNpa(),
                workshop.getLocation().getCity(),
                workshop.getImage());
    }

    public void transferClosed() {
        terminatedRepository.toClosed();
    }
    // ================

    // ================
    // Delete
    public Workshop duplicate(WorkshopId oldId, WorkshopId newId) {
        Workshop original = get(oldId);
        if (original == null) return null;

        Workshop created = null;
        if (original instanceof WorkshopDraft) {
            draftRepository.duplicate(oldId.getTitle(),
                    oldId.getDate(),
                    newId.getTitle(),
                    newId.getDate());

            created = get(newId);
        } else if (original instanceof WorkshopTerminated) {
            terminatedRepository.duplicate(oldId.getTitle(),
                    oldId.getDate(),
                    newId.getTitle(),
                    newId.getDate());

            created = get(newId);
        }

        return created;
    }
    // ================

    // ================
    // Others
    public List<Workshop> getAll(String key, Object value) {
        List<Workshop> workshops = new ArrayList<>();
        workshops.addAll(draftRepository.findAll(new WorkshopDraftSpecification(key, value)));
        workshops.addAll(terminatedRepository.findAll(new WorkshopTerminatedSpecification(key, value)));
        return workshops;
    }

    public List<Workshop> getAll(String key) {
        List<Workshop> workshops = new ArrayList<>();
        workshops.addAll(draftRepository.findAll(Sort.by(key)));
        workshops.addAll(terminatedRepository.findAll(Sort.by(key)));
        return workshops;
    }

    public List<WorkshopRestricted> getAllRestricted(String key) {
        return restrictedRepository.findAll(Sort.by(key));
    }

    public List<Workshop> getAll(WorkshopState state, String key) {
        List<Workshop> workshops = new ArrayList<>();

        if (state == WorkshopState.draft) {
            workshops.addAll(draftRepository.findAll(Sort.by(key)));
        } else {
            workshops.addAll(terminatedRepository.findAllByStateIs(state, Sort.by(key)));
        }

        return workshops;
    }

    public List<Workshop> getAll(WorkshopState state, String key, String value) {
        List<Workshop> workshops = new ArrayList<>();

        if (state == WorkshopState.draft) {
            workshops.addAll(draftRepository.findAll(new WorkshopDraftSpecification(key, value)));
        } else {
            workshops.addAll(terminatedRepository.findAll(
                    Specification.where(new WorkshopTerminatedSpecification("state", state))
                            .and(new WorkshopTerminatedSpecification(key, value))));
        }

        return workshops;
    }

    // ================
    public ByteArrayInputStream generatePDFUpcoming() {
        List<WorkshopTerminated> all = terminatedRepository.findAll(specificationFetchUpcoming);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Create pdf title information
            String headerText = "Liste de tous les ateliers qui auront lieu";
            Paragraph header = new Paragraph(headerText, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            header.setSpacingAfter(12);

            document.add(header);

            // Create table with all subs
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            addPDFUpcomingTable(table, all);

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addPDFUpcomingTable(PdfPTable table, List<WorkshopTerminated> all) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Stream.of("Titre", "Date", "Organisateur", "Catégorie", "Nombre participants")
                .forEach(name -> {
                    PdfPCell cell = new PdfPCell(new Phrase(name, headerFont));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                });

        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
        all.forEach(workshop -> {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(workshop.getTitle(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getDate().toString(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getEmailOrganizer(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getCategory(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getRegistrations().size() + "", contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        });
    }

    public ByteArrayInputStream generatePDFClientList(String email) {
        List<WorkshopTerminated> all = terminatedRepository.findAll((root, q, b) -> {
            Join<WorkshopTerminated, WorkshopRegistration> registrations = root.join("registrations");
            return q.where(b.equal(registrations.get("emailClient"), email)).getRestriction();
        });

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Create pdf title information
            String headerText = String.format("Liste des inscriptions");
            Paragraph header = new Paragraph(headerText, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            header.setSpacingAfter(12);

            document.add(header);

            // Create table with all subs
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            addPDFClientListTable(table, all);

            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addPDFClientListTable(PdfPTable table, List<WorkshopTerminated> all) {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Stream.of("Titre", "Date", "Catégorie")
                .forEach(name -> {
                    PdfPCell cell = new PdfPCell(new Phrase(name, headerFont));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                });

        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 7);
        all.forEach(workshop -> {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(workshop.getTitle(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getDate().toString(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(workshop.getCategory(), contentFont));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        });
    }
}
