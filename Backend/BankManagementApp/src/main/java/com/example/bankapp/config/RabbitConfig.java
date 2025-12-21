package com.example.bankapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {


	//JSON message converter
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}


	//rabbittemplate with JSON
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(jsonMessageConverter());
		return template;

	}

	@Bean
	public Queue otpQueue() {

		return QueueBuilder.durable(RabbitMQConstants.OTP_QUEUE)
				.withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", RabbitMQConstants.OTP_DLQ)
				.build();
	}

	@Bean
    public Queue otpDlq() {
        return QueueBuilder.durable(RabbitMQConstants.OTP_DLQ).build();
    }

    // 4️ Topic Exchange
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(RabbitMQConstants.NOTIFICATION_EXCHANGE);
    }

    // 5️ Binding OTP Queue to Exchange
    @Bean
    public Binding otpBinding(Queue otpQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(otpQueue)
                .to(notificationExchange)
                .with(RabbitMQConstants.OTP_ROUTING_KEY);
    }

}
