package by.baraznov.recruiting.util;


import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.services.PersonDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class PersonValidator implements Validator {
    private final PersonDetailsService personDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        try {
            personDetailsService.loadUserByUsername(person.getLogin());
        }catch(UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("login", "", "Такой логин уже существует");
    }
}
