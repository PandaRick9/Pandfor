package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.PeopleRepository;
import by.baraznov.recruiting.services.PeopleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PeopleServiceImpl implements PeopleService {
    private final PeopleRepository peopleRepository;
    @Override
    public List<Person> getAllUsers(String login) {
        Person person = peopleRepository.findByLogin(login).orElse(null);
        return peopleRepository.findAllWithoutOne(person.getUserId());
    }

    @Override
    @Transactional
    public void toggleActive(Integer id) {
        Person person = peopleRepository.findById(id).orElseThrow();
        person.setIsActive(!person.getIsActive());
        peopleRepository.save(person);
    }

}
