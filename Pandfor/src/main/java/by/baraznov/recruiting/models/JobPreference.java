package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "jobpreference")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class JobPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Integer preferenceId;
    @Column(name = "schedule")
    private String schedule;
    @Column(name = "employment_type")
    private String employmentType;
    @Column(name = "work_format")
    private String workFormat;
    @Column(name = "desired_salary")
    private Integer desiredSalary;
    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
}
