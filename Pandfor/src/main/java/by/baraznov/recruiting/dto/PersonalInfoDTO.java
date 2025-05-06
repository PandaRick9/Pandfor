package by.baraznov.recruiting.dto;

import lombok.Data;

@Data
public class PersonalInfoDTO {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String birthDate;
    private String phone;
    private String email;
    private String city;
    private String gender;
    private String workExperienceSummary;
}