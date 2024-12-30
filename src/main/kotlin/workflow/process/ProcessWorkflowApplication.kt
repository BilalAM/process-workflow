package workflow.process

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProcessWorkflowApplication

fun main(args: Array<String>) {

    runApplication<ProcessWorkflowApplication>(*args)
}