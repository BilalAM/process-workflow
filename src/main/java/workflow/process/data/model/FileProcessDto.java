package workflow.process.data.model;

public record FileProcessDto(
        String fileName,
        String status,
        String updatedAt
) {

}
