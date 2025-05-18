package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.dto.MatchJobConditionDTO;
import by.baraznov.recruiting.dto.MatchJobPreferenceDTO;
import by.baraznov.recruiting.dto.MatchPercentageDTO;
import by.baraznov.recruiting.dto.MatchResumeSkillDTO;
import by.baraznov.recruiting.dto.MatchVacancySkillDTO;
import by.baraznov.recruiting.dto.ResumeSkillDTO;
import by.baraznov.recruiting.dto.VacancyCardDTO;
import by.baraznov.recruiting.dto.VacancySkillDTO;
import by.baraznov.recruiting.models.Company;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import by.baraznov.recruiting.repositories.VacancyRepository;
import by.baraznov.recruiting.services.VacancyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    @Override
    @Transactional
    public void save(Vacancy vacancy) {
        vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        vacancyRepository.deleteById(id);
    }

    @Override
    public Vacancy findOne(Integer id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<VacancyCardDTO> findAllVacancies(Integer companyId) {
        return vacancyRepository.findAllByCompany(companyId);
    }
    public List<MatchPercentageDTO> findAllMatchPercentage(Integer vacancyId) {
        List<MatchResumeSkillDTO> allCandidateSkills = vacancyRepository.findCandidateSkillsByVacancyId(vacancyId);
        List<MatchVacancySkillDTO> requiredSkills = vacancyRepository.findVacancySkills(vacancyId);
        List<MatchJobPreferenceDTO> jobPreferences = vacancyRepository.findJobPreferencesByVacancyId(vacancyId);
        MatchJobConditionDTO jobCondition = vacancyRepository.findJobConditionByVacancyId(vacancyId);

        Map<Integer, List<MatchResumeSkillDTO>> skillsByResume = allCandidateSkills.stream()
                .collect(Collectors.groupingBy(MatchResumeSkillDTO::resumeId));

        Map<Integer, MatchJobPreferenceDTO> prefsByResumeId = jobPreferences.stream()
                .collect(Collectors.toMap(MatchJobPreferenceDTO::resumeId, jp -> jp));

        List<MatchPercentageDTO> result = new ArrayList<>();

        for (Map.Entry<Integer, List<MatchResumeSkillDTO>> entry : skillsByResume.entrySet()) {
            Integer resumeId = entry.getKey();
            List<MatchResumeSkillDTO> candidateSkills = entry.getValue();
            MatchJobPreferenceDTO jobPref = prefsByResumeId.get(resumeId);

            result.add(new MatchPercentageDTO(candidateSkills, requiredSkills, jobPref, jobCondition));
        }

        return result;
    }

}
