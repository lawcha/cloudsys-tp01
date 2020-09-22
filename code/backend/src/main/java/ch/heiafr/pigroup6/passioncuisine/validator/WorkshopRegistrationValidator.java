package ch.heiafr.pigroup6.passioncuisine.validator;

import ch.heiafr.pigroup6.passioncuisine.model.workshop.WorkshopRegistration;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class WorkshopRegistrationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return WorkshopRegistration.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailClient", "error.emailClient", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titleWorkshop", "error.titleWorkshop", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateWorkshop", "error.dateWorkshop", "field is empty");
    }
}
