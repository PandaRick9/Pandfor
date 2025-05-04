package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Person;
import by.baraznov.recruiting.repositories.PeopleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PersonService {
    private final PeopleRepository peopleRepository;
    public Person findOne(int id){
        return peopleRepository.findById(id).orElse(null);
    }
}
