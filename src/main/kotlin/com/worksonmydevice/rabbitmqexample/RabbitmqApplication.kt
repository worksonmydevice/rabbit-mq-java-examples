package com.worksonmydevice.rabbitmqexample

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class RabbitmqApplication {
    @Bean
    open fun queue(): Queue {
        val queue = Queue(queueName, false)
        queue.addArgument("x-dead-letter-exchange", topicExchangeName)
        queue.addArgument("x-dead-letter-routing-key", EXAMPLE_REJECT)
        return queue
    }

    @Bean
    open fun allMessagesQueue(): Queue {
        return Queue("all-messages", false)
    }

    @Bean
    open fun deadLetterQueue(): Queue {
        return Queue(DEAD_MESSAGES, false)
    }

    @Bean
    open fun exchange(): TopicExchange {
        return TopicExchange(topicExchangeName)
    }

    @Bean
    open fun binding(queue: Queue?, exchange: TopicExchange?): Binding {
        return BindingBuilder.bind(queue).to(exchange).with("example.binding.#")
    }

    @Bean
    open fun allBinding(allMessagesQueue: Queue?, exchange: TopicExchange?): Binding {
        return BindingBuilder.bind(allMessagesQueue).to(exchange).with("#")
    }

    @Bean
    open fun deadLetterBinding(deadLetterQueue: Queue?, exchange: TopicExchange?): Binding {
        return BindingBuilder.bind(deadLetterQueue).to(exchange).with(EXAMPLE_REJECT)
    }

    @Bean
    open fun container(
        connectionFactory: ConnectionFactory?,
        listenerAdapter: MessageListenerAdapter?
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(queueName)
        container.setMessageListener(listenerAdapter)
        container.setDefaultRequeueRejected(false)
        return container
    }

    @Bean
    open fun listenerAdapter(receiver: Receiver?): MessageListenerAdapter {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

    companion object {
        const val topicExchangeName = "example-exchange"
        const val queueName = "example-queue"
        const val DEAD_MESSAGES = "dead-messages"
        const val EXAMPLE_REJECT = "example-reject"

        @Throws(InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(RabbitmqApplication::class.java, *args).close()
        }
    }
}