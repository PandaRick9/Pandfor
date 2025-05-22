package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.MatchWeights;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchWeightsRepository extends JpaRepository<MatchWeights, Integer> {
}
