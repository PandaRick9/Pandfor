package by.baraznov.recruiting.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;


    @Column(name = "data", columnDefinition="bytea")
    private byte[] data;

    @OneToOne(mappedBy = "photo")
    private PersonalInfo personalinfo;
    @OneToOne(mappedBy = "photo")
    private Company company;
    @OneToOne(mappedBy = "photo")
    private JobSeeker jobSeeker;

}