package workflow.process.configuration;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EventsBrokerConfiguration {

    @Bean
    public Queue queue() {
        return new Queue("file-process", true);
    }

    @Bean
    public SimpleMessageConverter simpleMessageConverter() {
        SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
        simpleMessageConverter.setAllowedListPatterns(List.of("workflow.process.events.*"));
        return simpleMessageConverter;
    }
}
