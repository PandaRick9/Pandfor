package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "employer")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employer_id")
    private Integer employerId;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @OneToMany(mappedBy = "employer")
    private List<Vacancy> vacancies;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
