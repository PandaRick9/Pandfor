package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Person;

import java.util.List;

public interface PeopleService {
    List<Person> getAllUsers(String login);
    void toggleActive(Integer id);
}
