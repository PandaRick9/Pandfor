package by.baraznov.recruiting.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attachment")
@Setter
@Getter
@RequiredArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Integer attachmentId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "data", columnDefinition="bytea")
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "reaction_id")
    private Reaction reaction;
}
