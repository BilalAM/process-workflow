package workflow.process.data.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@Table(name = "outbox", schema = "public")
@Entity
public class Outbox {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "file-uuid")
    private String fileUUID;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private OffsetDateTime updatedAt;
}
