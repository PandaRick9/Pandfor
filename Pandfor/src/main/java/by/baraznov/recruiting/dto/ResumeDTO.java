package by.baraznov.recruiting.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResumeDTO {
    private String jobTitle;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private String birthDate;
    private String phone;
    private String email;
    private String city;
    private String workDescription;
    private String schoolName;
    private String speciality;
    private String graduationYear;
    private String workSchedule;
    private String employmentType;
    private String workFormat;
    private String experience;
    private String salary;
    private List<String> skills;
}
