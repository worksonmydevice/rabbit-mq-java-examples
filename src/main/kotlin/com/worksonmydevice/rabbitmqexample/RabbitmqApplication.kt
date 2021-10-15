package com.worksonmydevice.rabbitmqexample;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqApplication {
    static final String topicExchangeName = "example-exchange";

    static final String queueName = "example-queue";
    public static final String DEAD_MESSAGES = "dead-messages";
    public static final String EXAMPLE_REJECT = "example-reject";

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(RabbitmqApplication.class, args).close();
    }

    @Bean
    Queue queue() {
        final Queue queue = new Queue(queueName, false);
        queue.addArgument("x-dead-letter-exchange", topicExchangeName);
        queue.addArgument("x-dead-letter-routing-key", EXAMPLE_REJECT);
        return queue;
    }

    @Bean
    Queue allMessagesQueue() {
        return new Queue("all-messages", false);
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue(DEAD_MESSAGES, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("example.binding.#");
    }

    @Bean
    Binding allBinding(Queue allMessagesQueue, TopicExchange exchange) {
        return BindingBuilder.bind(allMessagesQueue).to(exchange).with("#");
    }

    @Bean
    Binding deadLetterBinding(Queue deadLetterQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deadLetterQueue).to(exchange).with(EXAMPLE_REJECT);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setDefaultRequeueRejected(false);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
