package by.baraznov.recruiting.mappers;

import by.baraznov.recruiting.dto.EducationDTO;
import by.baraznov.recruiting.dto.JobPreferenceDTO;
import by.baraznov.recruiting.dto.PersonalInfoDTO;
import by.baraznov.recruiting.dto.ResumeDTO;
import by.baraznov.recruiting.dto.ResumeSkillDTO;
import by.baraznov.recruiting.models.Education;
import by.baraznov.recruiting.models.JobPreference;
import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.models.Resume;
import by.baraznov.recruiting.models.ResumeSkill;
import by.baraznov.recruiting.models.Skill;
import by.baraznov.recruiting.repositories.SkillRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", imports = {LocalDate.class, DateTimeFormatter.class, LocalDateTime.class})
public interface ResumeMapper {

    Resume toEntity(ResumeDTO dto);

    @Mapping(target = "birthDate", expression = "java(LocalDate.parse(dto.getBirthDate(), DateTimeFormatter.ofPattern(\"dd.MM.yyyy\")))")
    PersonalInfo toEntity(PersonalInfoDTO dto);

    Education toEntity(EducationDTO dto);

    List<Education> toEducationList(List<EducationDTO> dtoList);



    JobPreference toEntity(JobPreferenceDTO dto);




    @Mapping(target = "resumeSkillId", ignore = true)
    @Mapping(target = "resume", ignore = true)
    @Mapping(target = "skill", ignore = true)
    @Mapping(source = "proficiencyLevel", target = "proficiencyLevel")
    ResumeSkill toEntity(ResumeSkillDTO dto);

    List<ResumeSkill> toEntityList(List<ResumeSkillDTO> dtoList);
}