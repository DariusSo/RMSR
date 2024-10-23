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
            jedis.set(orderId.getBytes(), serialize(value));
            System.out.println("Added Order (Redis): " + orderId);
        }
    }

    public Object get(String key) throws IOException, ClassNotFoundException {
        try (Jedis jedis = jedisPool.getResource()) {
            byte[] data = jedis.get(key.getBytes());
            if (data != null) {
                return deserialize(data);
            }
            return null;
        }
    }

    private byte[] serialize(Object obj) throws IOException {

        byte[] json = objectMapper.writeValueAsString(obj).getBytes();
        return json;
    }

    private List<Integer> deserialize(byte[] data) throws IOException, ClassNotFoundException {

        List<Integer> obj = objectMapper.readValue(data, List.class);
        return obj;
    }

    public void close() {
        jedisPool.close();
    }
}
