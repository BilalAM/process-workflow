package workflow.process.data;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;


@Entity
@Table(name = "file_process")
data class FileProcess(

    @Id
    @ColumnDefault("nextval('file_process_id_seq')")
    @Column(name = "id", nullable = false)
    var id: Int? = null,

    @Column(name = "file_name", nullable = false, length = Int.MAX_VALUE)
    var fileName: String? = null,

    @Column(name = "s3_url", length = Int.MAX_VALUE)
    var s3Url: String? = null,

    @Column(name = "status", nullable = false, length = Int.MAX_VALUE)
    var status: String? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime? = null
)
