package by.baraznov.recruiting.models;

import by.baraznov.recruiting.models.enums.EmploymentType;
import by.baraznov.recruiting.models.enums.ExperienceYear;
import by.baraznov.recruiting.models.enums.Schedule;
import by.baraznov.recruiting.models.enums.WorkFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jobconditions")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class JobCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Integer conditionId;
    @Column(name = "schedule")
    @Enumerated(EnumType.STRING)
    private Schedule schedule;
    @Column(name = "employment_type")
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;
    @Column(name = "work_format")
    @Enumerated(EnumType.STRING)
    private WorkFormat workFormat;
    @Column(name = "required_experience_years")
    @Enumerated(EnumType.STRING)
    private ExperienceYear requiredExperienceYears;
    @OneToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

}
