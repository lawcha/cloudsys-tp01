package ch.heiafr.pigroup6.passioncuisine.services;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopId;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRegistration;
import ch.heiafr.pigroup6.passioncuisine.repository.workshop.WorkshopTerminatedRepository;
import ch.heiafr.pigroup6.passioncuisine.repository.workshop.WorkshopRegistrationRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class WorkshopRegistrationService {

    private final WorkshopTerminatedRepository publishedRepository;
    private final WorkshopRegistrationRepository registrationRepository;

    public WorkshopRegistrationService(WorkshopTerminatedRepository publishedRepository, WorkshopRegistrationRepository registrationRepository) {
        this.publishedRepository = publishedRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<WorkshopRegistration> getAll(WorkshopId workshopId) {
        if (!publishedRepository.existsById(workshopId)) return null;

        return registrationRepository.findAllByDateWorkshopEqualsAndTitleWorkshopEquals(workshopId.getDate(), workshopId.getTitle());
    }

    public Optional<WorkshopRegistration> get(WorkshopRegistration registration) {
        Optional<WorkshopRegistration> found = registrationRepository.findById(registration.getId());
        return found;
    }

    public WorkshopRegistration save(WorkshopRegistration registration) {
        WorkshopId workshopId = new WorkshopId(registration.getTitleWorkshop(), registration.getDateWorkshop());
        if (!publishedRepository.existsById(workshopId)) return null;

        registrationRepository.save(registration.getEmailClient(), registration.getDateWorkshop(), registration.getTitleWorkshop());
        return registration;
    }

    public void delete(WorkshopRegistration registration) {
        registrationRepository.delete(registration);
    }

    public ByteArrayInputStream generatePDFRegistrationList(WorkshopId id) {
        List<WorkshopRegistration> all = getAll(id);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Create pdf title information
            String headerText = String.format("Inscriptions pour l'atelier \"%s\" se déroulant le %s", id.getTitle(), id.getDate());
            Paragraph header = new Paragraph(headerText, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            header.setSpacingAfter(12);

            document.add(header);

            // Create table with all subs
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            addPDFHeaderRegistrationList(table);
            addPDFContentRegistrationList(table, all);

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

    private void addPDFHeaderRegistrationList(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Stream.of("Nom", "Prénom", "Email", "Téléphone")
                .forEach(name -> {
                    PdfPCell cell = new PdfPCell(new Phrase(name, font));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                });
    }

    private void addPDFContentRegistrationList(PdfPTable table, List<WorkshopRegistration> all) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 7);
        all.forEach(registration -> {
            Client client = registration.getRegisteredClient();

            PdfPCell cell;

            cell = new PdfPCell(new Phrase(client.getFirstName(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(client.getLastName(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(client.getEmail(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(client.getPhone(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        });
    }

}
