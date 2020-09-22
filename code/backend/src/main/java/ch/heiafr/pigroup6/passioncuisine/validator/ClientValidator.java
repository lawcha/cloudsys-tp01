package ch.heiafr.pigroup6.passioncuisine.validator;

import ch.heiafr.pigroup6.passioncuisine.model.users.Client;
import ch.heiafr.pigroup6.passioncuisine.model.users.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ClientValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.email", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "error.password", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "error.firstName", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.lastName", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "error.phone", "field is empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "error.street", "field is empty");

        Client client = (Client) target;

        if (!client.getEmail().matches("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$")) {
            errors.rejectValue("email", "invalid email values, should be of format abc@def.xy");
        }

        if (!(client.getPhone().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))) {
            errors.rejectValue("phone", "invalid phone number, should be of format 0790000000");
        }
    }
}
