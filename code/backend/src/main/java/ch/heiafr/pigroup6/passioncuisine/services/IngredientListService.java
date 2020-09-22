package ch.heiafr.pigroup6.passioncuisine.services;

import ch.heiafr.pigroup6.passioncuisine.model.IngredientList;
import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopTerminated;
import ch.heiafr.pigroup6.passioncuisine.repository.workshop.WorkshopTerminatedRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class IngredientListService {
    private final WorkshopTerminatedRepository publishedRepository;

    public IngredientListService(WorkshopTerminatedRepository publishedRepository) {
        this.publishedRepository = publishedRepository;
    }

    public List<IngredientList> getAll(LocalDate startDate, LocalDate endDate) {
        List<WorkshopTerminated> workshops = publishedRepository.findAllByDateIsBetween(startDate, endDate);

        List<IngredientList> ingredientLists = new ArrayList<>();

        for (WorkshopTerminated item : workshops) {
            ingredientLists.add(new IngredientList(
                    item.getTitle(),
                    item.getDate(),
                    item.getRegistrations().size(),
                    item.getIngredients()
            ));
        }

        return ingredientLists;
    }

    public ByteArrayInputStream generatePDF(LocalDate startDate, LocalDate endDate) {
        List<IngredientList> all = getAll(startDate, endDate);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Create pdf title information
            String headerText = String.format("Liste des ingrédients pour les ateliers du %s au %s", startDate, endDate);
            Paragraph header = new Paragraph(headerText, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
            header.setSpacingAfter(12);

            document.add(header);

            // Create table with all subs
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            addPdfTableHeader(table);
            addPdfTableContent(table, all);

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

    private void addPdfTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
        Stream.of("Titre", "Date", "Nombre de fois", "Ingrédients")
                .forEach(name -> {
                    PdfPCell cell = new PdfPCell(new Phrase(name, font));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cell);
                });
    }

    private void addPdfTableContent(PdfPTable table, List<IngredientList> all) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 7);
        all.forEach(ingredients -> {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(ingredients.getTitle(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ingredients.getDate().toString(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ingredients.getCount() + "", font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(ingredients.getIngredients(), font));
            cell.setBackgroundColor(BaseColor.WHITE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        });
    }
}
