package by.baraznov.recruiting.dto;

import by.baraznov.recruiting.models.Photo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class CompanyDTO {
    private String name;
    private String description;
    private String city;

}
