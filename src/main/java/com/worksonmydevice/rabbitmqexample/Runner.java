package com.worksonmydevice.rabbitmqexample;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        sendOkMessage();
        sendBadMessage();
    }

    private void sendOkMessage() throws UnsupportedEncodingException, InterruptedException {
        System.out.println("Sending message...");
        final byte[] body = "OK message".getBytes("UTF-8");
        final MessageProperties props = new MessageProperties();
        props.setContentType("text");
        props.setContentEncoding("UTF-8");
        final Message message = new Message(body, props);
        rabbitTemplate.send(RabbitmqApplication.topicExchangeName, "example.binding.ok", message);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

    private void sendBadMessage() throws UnsupportedEncodingException, InterruptedException {
        System.out.println("Sending message...");
        final byte[] body = "BAD message".getBytes("UTF-8");
        final MessageProperties props = new MessageProperties();
        props.setContentType("text");
        props.setContentEncoding("UTF-8");
        final Message message = new Message(body, props);
        rabbitTemplate.send(RabbitmqApplication.topicExchangeName, "example.binding.bad", message);
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

}