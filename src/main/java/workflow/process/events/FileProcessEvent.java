package workflow.process.events;

import lombok.Data;

import java.io.Serializable;


@Data
public class FileProcessEvent implements Serializable {
    String fileName;
}