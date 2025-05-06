package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.ResumeSkill;
import by.baraznov.recruiting.repositories.ResumeSkillRepository;
import by.baraznov.recruiting.services.ResumeSkillService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ResumeSkillServiceImpl implements ResumeSkillService {
    private final ResumeSkillRepository resumeSkillRepository;
    @Override
    @Transactional
    public void saveResumeSkill(ResumeSkill resumeSkill) {
        resumeSkillRepository.save(resumeSkill);
    }
}
