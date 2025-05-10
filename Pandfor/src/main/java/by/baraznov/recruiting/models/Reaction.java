package by.baraznov.recruiting.models;

import by.baraznov.recruiting.models.enums.ReactionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "reaction")
@Setter
@Getter
@RequiredArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Integer reactionId;
    @Column(name = "cover_letter")
    private String coverLetter;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReactionStatus status;
    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "reaction", cascade = CascadeType.ALL)
    private Attachment attachment;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;
    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    private Vacancy vacancy;
}
