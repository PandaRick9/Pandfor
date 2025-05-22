package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.PeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Person person, boolean isJobSeeker){
        person.setPassword( passwordEncoder.encode(person.getPassword()));
        person.setIsActive(true);
        if(isJobSeeker){
            person.setRole("ROLE_JOBSEEKER");
        }else {
            person.setRole("ROLE_COMPANY");
        }
        peopleRepository.save(person);
    }
    public boolean registerAdmin(String login, String rawPassword) {
        if (peopleRepository.findByLogin(login).isPresent()) {
            return false;
        }

        Person admin = new Person();
        admin.setLogin(login);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setRole("ROLE_ADMIN");
        peopleRepository.save(admin);
        return true;
    }
}
