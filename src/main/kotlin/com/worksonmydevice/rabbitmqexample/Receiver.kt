package com.worksonmydevice.rabbitmqexample

import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class Receiver {
    val latch = CountDownLatch(1)
    fun receiveMessage(message: String) {
        println("Received <$message>")
        latch.countDown()
        if (message.startsWith("BAD")) {
            throw AmqpRejectAndDontRequeueException("don't want this message")
        }
    }
}