package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.PeopleRepository;
import by.baraznov.recruiting.security.PersonDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person =  peopleRepository.findByLogin(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден");
        return new PersonDetails(person.get());
    }
}
