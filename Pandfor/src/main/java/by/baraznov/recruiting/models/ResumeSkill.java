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
@Table(name = "resumeskill", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"resume_id", "skill_id"})
})
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class ResumeSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_skill_id")
    private Integer resumeSkillId;
    @Column(name = "proficiency_level")
    private String proficiencyLevel;
    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}
