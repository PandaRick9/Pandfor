package by.baraznov.recruiting.mappers;

import by.baraznov.recruiting.dto.JobConditionDTO;
import by.baraznov.recruiting.dto.VacancyDTO;
import by.baraznov.recruiting.dto.VacancySkillDTO;
import by.baraznov.recruiting.models.JobCondition;
import by.baraznov.recruiting.models.Vacancy;
import by.baraznov.recruiting.models.VacancySkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    Vacancy toEntity(VacancyDTO dto);

    JobCondition toEntity(JobConditionDTO dto);

    @Mappings({
            @Mapping(target = "vacancySkillId", ignore = true),
            @Mapping(target = "vacancy", ignore = true),
            @Mapping(target = "skill", ignore = true),
            @Mapping(source = "requiredLevel", target = "requiredLevel")
    })
    VacancySkill toEntity(VacancySkillDTO dto);

    List<VacancySkill> toEntityList(List<VacancySkillDTO> dtoList);
}