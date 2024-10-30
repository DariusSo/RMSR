package org.example;

public class Main {
    public static void main(String[] args) {

        Thread waitingOrders = new Thread(new RabbitServiceWaitingOrders());
        Thread orderChange = new Thread(new RabbitServiceChangeStatus());
        Thread checkFailedOrders = new Thread(new CheckDB());
        Thread waitingPayment = new Thread(new RabbitServicePayment());

        checkFailedOrders.start();
        waitingOrders.start();
        orderChange.start();
        waitingPayment.start();
    }
}