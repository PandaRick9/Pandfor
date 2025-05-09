package by.baraznov.recruiting.mappers;

import by.baraznov.recruiting.dto.CompanyDTO;
import by.baraznov.recruiting.models.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company toEntity(CompanyDTO dto);

}