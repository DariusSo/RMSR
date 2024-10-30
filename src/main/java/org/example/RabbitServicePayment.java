package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitServicePayment implements Runnable{
    private static final String HOST = "localhost";
    private final ConnectionFactory factory;
    private final ObjectMapper objectMapper;
    MongoDBRepository mongoDBRepository = new MongoDBRepository();
    RedisService redisService = new RedisService("localhost", 6379);

    private long recievedCount = 0;

    public RabbitServicePayment() {
        this.factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        this.objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Override
    public void run() {
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {

            channel.queueDeclare("Payment_queue", false, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String jsonMessage = new String(delivery.getBody(), "UTF-8");
                System.out.println(recievedCount+" Got JSON: " + jsonMessage);
                recievedCount++;
                try {
                    Map<String, String> info = objectMapper.readValue(jsonMessage, Map.class);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    mongoDBRepository.acceptPayment(info.get("orderId"), info.get("paymentMethod"), Double.parseDouble(info.get("totalPrice")));
                    redisService.acceptPayment(info.get("orderId"), info.get("paymentMethod"), Double.parseDouble(info.get("totalPrice")));

                }catch (Exception e){
                    e.printStackTrace();

                    channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                }
            };
            channel.basicConsume("Payment_queue", false, deliverCallback, consumerTag -> {});

            System.out.println("Waiting");
            while(true) {
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
