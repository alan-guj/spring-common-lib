package top.jyx365.configuration.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import top.jyx365.amqp.AmqpPublisherInterceptor;
import top.jyx365.amqp.AmqpSender;



@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
public class AmqpConfiguration {

    @Autowired
    AmqpProperties config;

    @Bean
    public RabbitTemplate template(ConnectionFactory connectionFactory, ObjectMapper mapper) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setJsonObjectMapper(mapper);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(config.getTopic());
    }

    @Bean
    public AmqpSender amqpSender() {
        return new AmqpSender();
    }

    @Bean
    public AmqpPublisherInterceptor interceptor() {
        return new AmqpPublisherInterceptor();
    }


}

@ConfigurationProperties(prefix = "amqp")
class AmqpProperties {
    private String topic;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}


