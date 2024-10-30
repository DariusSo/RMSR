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
    private double totalPrice;
    private String paymentMethod;

    public Order(String orderId, int table, String client, List<String> dishes, LocalDateTime orderTime, String status, LocalDateTime proccessingTime, double totalPrice, String paymentMethod) {
        this.orderId = orderId;
        this.table = table;
        this.client = client;
        this.dishes = dishes;
        this.orderTime = orderTime;
        this.status = status;
        this.proccessingTime = proccessingTime;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public Order() {
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}