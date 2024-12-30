package workflow.process.data;


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime


@Entity
@Table(name = "file_process", schema = "public")
data class FileProcess(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "file_name", nullable = false, length = Int.MAX_VALUE)
    var fileName: String? = null,

    @Column(name = "s3_url", length = Int.MAX_VALUE)
    var s3Url: String? = null,

    @Column(name = "status", nullable = false, length = Int.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    var status: FileStatus? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime? = null
)
