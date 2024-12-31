package workflow.process.data.model

import workflow.process.data.FileStatus
import java.time.OffsetDateTime;

data class FileProcessDto(
    var fileName: String,
    var s3Url: String?,
    var status: FileStatus,
    var createdAt: OffsetDateTime? = null,
    var updatedAt: OffsetDateTime? = null
)