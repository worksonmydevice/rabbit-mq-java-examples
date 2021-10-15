package com.worksonmydevice.rabbitmqexample;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) {
      System.out.println("Received <" + message + ">");
      latch.countDown();
      if (message.startsWith("BAD")) {
          throw new AmqpRejectAndDontRequeueException("don't want this message");
      }
  }

  public CountDownLatch getLatch() {
    return latch;
  }

}