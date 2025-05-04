package by.baraznov.recruiting.models;

import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "education")
@Setter
@Getter
@RequiredArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "education_id")
    private Integer educationId;
    @Column(name = "institution")
    private String institution;
    @Column(name = "specialization")
    private String specialization;
    @Column(name = "graduation_date")
    private Date graduationDate;
    @ManyToOne
    @JoinColumn(name = "info_id")
    private PersonalInfo personalInfo;
}
