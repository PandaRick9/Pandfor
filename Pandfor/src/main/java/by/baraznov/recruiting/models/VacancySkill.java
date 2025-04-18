package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vacancyskill", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"vacancy_id", "skill_id"})
})
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class VacancySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vacancy_skill_id")
    private Integer vacancySkillId;
    @Column(name = "required_level")
    private String requiredLevel;
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
