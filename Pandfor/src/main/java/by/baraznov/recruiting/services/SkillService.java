package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Skill;

import java.util.Optional;

public interface SkillService {
    Skill save(Skill skill);
    Optional<Skill> findByName(String name);
}
