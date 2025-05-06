package by.baraznov.recruiting.repositories;

import by.baraznov.recruiting.models.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Integer> {
}
