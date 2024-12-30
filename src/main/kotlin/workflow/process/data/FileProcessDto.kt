package workflow.process.data;

import java.time.OffsetDateTime;

data class FileProcessDto(
    var fileName: String,
    var s3Url: String?,
    var status: FileStatus,
    var createdAt: OffsetDateTime,
    var updatedAt: OffsetDateTime
)