package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String id;
    private String orderId;
    private String client;
    private int table;
    private List<String> dishes;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime proccessingTime;

    public Order(String id, String orderId, String client, int table, List<String> dishes, String status, LocalDateTime orderTime, LocalDateTime proccessingTime) {
        this.id = id;
        this.orderId = orderId;
        this.client = client;
        this.table = table;
        this.dishes = dishes;
        this.status = status;
        this.orderTime = orderTime;
        this.proccessingTime = proccessingTime;
    }



    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public List<String> getDishes() {
        return dishes;
    }

    public void setDishes(List<String> dishes) {
        this.dishes = dishes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getProccessingTime() {
        return proccessingTime;
    }

    public void setProccessingTime(LocalDateTime proccessingTime) {
        this.proccessingTime = proccessingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
