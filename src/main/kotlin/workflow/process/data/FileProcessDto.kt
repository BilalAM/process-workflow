package workflow.process.data;

import java.time.OffsetDateTime;

data class FileProcessDto(
    val id: Int,
    val fileName: String,
    val s3Url: String?,
    val status: String,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)