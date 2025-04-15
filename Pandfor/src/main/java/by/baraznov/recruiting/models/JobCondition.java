package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private String schedule;
    @Column(name = "employment_type")
    private String employmentType;
    @Column(name = "work_format")
    private String workFormat;
    @Column(name = "required_experience_years")
    private Integer requiredExperienceYears;
    @OneToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;

}
