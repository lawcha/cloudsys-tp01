package ch.heiafr.pigroup6.passioncuisine.validator;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopDraft;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.xml.transform.StringSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;

@Component
public class WorkshopDraftValidator implements Validator {

    javax.xml.validation.Validator ingredientsValidator;
    javax.xml.validation.Validator descriptionValidator;

    public WorkshopDraftValidator() throws IOException, SAXException {
        Resource ingredientsXSD = new ClassPathResource("ingredients_validator.xsd");
        SchemaFactory factoryIngredients = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schemaIngredients = factoryIngredients.newSchema(ingredientsXSD.getURL());
        ingredientsValidator = schemaIngredients.newValidator();

        Resource xhtmlXSD = new ClassPathResource("xhtml1-strict.xsd");
        SchemaFactory factoryDescription = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schemaDescription = factoryDescription.newSchema(xhtmlXSD.getURL());
        descriptionValidator = schemaDescription.newValidator();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return WorkshopDraft.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "error.title", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "error.date", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ingredients", "error.ingredients", "field is empty");

        WorkshopDraft workshop = (WorkshopDraft) target;

        try {
            ingredientsValidator.validate(new StringSource(workshop.getIngredients()));
        } catch (SAXException | IOException e) {
            errors.rejectValue("ingredients", "error.ingredients", e.getMessage());
        }

//        try {
//            descriptionValidator.validate(new StringSource(workshop.getDescription()));
//        } catch (SAXException | IOException e) {
//            errors.rejectValue("description", "error.description", e.getMessage());
//        }
    }
}
