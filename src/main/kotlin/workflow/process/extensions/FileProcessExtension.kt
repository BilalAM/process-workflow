package workflow.process.extensions

import workflow.process.data.model.FileProcess
import workflow.process.data.model.FileProcessDto

fun FileProcessDto.toEntity() = FileProcess(
    fileName = fileName,
    s3Url = s3Url,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun FileProcess.toDto() = FileProcessDto(
    fileName = fileName!!,
    s3Url = s3Url,
    status = status!!,
    createdAt = createdAt!!,
    updatedAt = updatedAt!!
)