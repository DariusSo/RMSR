package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.List;

public class RedisService {
    private final JedisPool jedisPool;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RedisService(String host, int port) {

        this.jedisPool = new JedisPool(host, port);
        objectMapper.findAndRegisterModules();
    }

    public void put(String orderId, Object value) throws IOException {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(orderId.getBytes(), 300, serialize(value));
            System.out.println("Added Order (Redis): " + orderId);
        }
    }

    public Order get(String key) throws IOException, ClassNotFoundException {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] data = jedis.get(key.getBytes());
            if (data != null) {
                return deserialize(data);
            }
            return null;
        }
    }
    public void updateOrder(Order order) throws IOException, ClassNotFoundException {
        Order fetchedOrder = get(order.getOrderId());
        if(fetchedOrder != null){
            fetchedOrder.setStatus(order.getStatus());
            put(fetchedOrder.getOrderId(), fetchedOrder);
        }
    }

    public void acceptPayment(String id, String paymentMethod, double totalPrice) throws IOException, ClassNotFoundException {
        Order fetchedOrder = get(id);
        if(fetchedOrder != null){
            fetchedOrder.setPaymentMethod(paymentMethod);
            fetchedOrder.setTotalPrice(totalPrice);
            put(fetchedOrder.getOrderId(), fetchedOrder);
        }
    }

    private byte[] serialize(Object obj) throws IOException {

        byte[] json = objectMapper.writeValueAsString(obj).getBytes();
        return json;
    }

    private Order deserialize(byte[] data) throws IOException, ClassNotFoundException {

        Order obj = objectMapper.readValue(data, Order.class);
        return obj;
    }

    public void close() {
        jedisPool.close();
    }
}
