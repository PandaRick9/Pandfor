package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
}
