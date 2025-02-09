package workflow.process;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
class ProcessWorkflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessWorkflowApplication.class, args);
    }
}