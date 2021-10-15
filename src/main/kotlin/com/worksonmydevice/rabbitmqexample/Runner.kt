package com.worksonmydevice.rabbitmqexample

import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.util.concurrent.TimeUnit

@Component
class Runner(private val receiver: Receiver, private val rabbitTemplate: RabbitTemplate) : CommandLineRunner {
    @Throws(Exception::class)
    override fun run(vararg args: String) {
        sendOkMessage()
        sendBadMessage()
    }

    @Throws(UnsupportedEncodingException::class, InterruptedException::class)
    private fun sendOkMessage() {
        println("Sending message...")
        val body = "OK message".toByteArray(charset("UTF-8"))
        val props = MessageProperties()
        props.contentType = "text"
        props.contentEncoding = "UTF-8"
        val message = Message(body, props)
        rabbitTemplate.send(RabbitmqApplication.Companion.topicExchangeName, "example.binding.ok", message)
        receiver.latch.await(10000, TimeUnit.MILLISECONDS)
    }

    @Throws(UnsupportedEncodingException::class, InterruptedException::class)
    private fun sendBadMessage() {
        println("Sending message...")
        val body = "BAD message".toByteArray(charset("UTF-8"))
        val props = MessageProperties()
        props.contentType = "text"
        props.contentEncoding = "UTF-8"
        val message = Message(body, props)
        rabbitTemplate.send(RabbitmqApplication.Companion.topicExchangeName, "example.binding.bad", message)
        receiver.latch.await(10000, TimeUnit.MILLISECONDS)
    }
}