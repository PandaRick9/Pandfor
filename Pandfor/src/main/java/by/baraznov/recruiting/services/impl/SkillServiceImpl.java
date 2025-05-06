package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.repositories.SkillRepository;
import by.baraznov.recruiting.services.SkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    @Override
    @Transactional
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }
}
