package by.baraznov.recruiting.services;

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
        if(isJobSeeker){
            person.setRole("ROLE_JOBSEEKER");
        }else {
            person.setRole("ROLE_COMPANY");
        }
        peopleRepository.save(person);
    }
}
