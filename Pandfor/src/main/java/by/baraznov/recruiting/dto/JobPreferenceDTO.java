package by.baraznov.recruiting.dto;

import lombok.Data;

@Data
public class JobPreferenceDTO {
    private String schedule;
    private String employmentType;
    private String workFormat;
    private Integer desiredSalary;
    private String experienceYear;
}

