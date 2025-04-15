package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "skill")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Integer skillId;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "skill")
    private List<ResumeSkill> resumeSkills;
    @OneToMany(mappedBy = "skill")
    private List<VacancySkill> vacancySkills;
}
