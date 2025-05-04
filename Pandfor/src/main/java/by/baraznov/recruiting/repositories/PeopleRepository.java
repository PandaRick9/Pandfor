package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByLogin(String username);
    @Query(value = "SELECT * FROM user_table WHERE id != :user_id", nativeQuery = true)
    List<Person> findAllWithoutOne(@Param("user_id") Integer user_id);
}
