package workflow.process.events;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventsBrokerConfig {
    @Bean
    fun converter(): org.springframework.amqp.support.converter.SimpleMessageConverter {
        val simpleMessageConverter = org.springframework.amqp.support.converter.SimpleMessageConverter();
        simpleMessageConverter.setAllowedListPatterns(listOf("workflow.process.events.*"));
        return simpleMessageConverter
    }
}
