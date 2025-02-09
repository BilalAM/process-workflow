package workflow.process.data.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;


@Entity
@Data
@Table(name = "file_process", schema = "public")
public class FileProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_s3_path")
    private String fileS3Path;

    @Column(name = "file-uuid")
    private String uuid;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private FileStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version")
    private Long version;
}
