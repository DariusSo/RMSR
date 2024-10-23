package org.example;

public class Main {
    public static void main(String[] args) {

        Thread waitingOrders = new Thread(new RabbitServiceWaitingOrders());
        Thread orderChange = new Thread(new RabbitServiceChangeStatus());

        waitingOrders.start();
        orderChange.start();
    }
}