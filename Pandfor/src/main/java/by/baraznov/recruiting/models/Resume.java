package by.baraznov.recruiting.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "resume")
@Setter
@Getter
@RequiredArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Integer resumeId;
    @Column(name = "title")
    private String title;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created")
    private LocalDateTime dateCreated;
    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "seeker_id")
    private JobSeeker jobSeeker;
    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private JobPreference jobPreference;

    @OneToOne(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private PersonalInfo personalInfo;
    @OneToMany(mappedBy = "resume")
    private List<Reaction> reactions;
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ResumeSkill> resumeSkills;
}
