package workflow.process.configuration;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsBrokerConfiguration {

    @Bean
    public Queue queue() {
        return new Queue("file-process", true);
    }
}
