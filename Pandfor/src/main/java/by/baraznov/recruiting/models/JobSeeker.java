package by.baraznov.recruiting.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "jobseeker")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class JobSeeker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seeker_id")
    private Integer seekerId;
    @OneToMany(mappedBy = "jobSeeker")
    private List<Resume> resumes;
    @OneToOne
    @JoinColumn(name = "user_id")
    private Person person;

}
