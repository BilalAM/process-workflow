package workflow.process.events

import java.io.Serializable

data class FileProcessEvent(
    val fileName: String
) : Serializable {
}