package by.baraznov.recruiting.util;


import by.baraznov.recruiting.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PasswordValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if(!person.getPassword().equals(person.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "","Пароли должны совпадать");
        }
    }
}
