package org.example;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CheckDB implements Runnable{
    MongoDBRepository mongoDBRepository = new MongoDBRepository();
    @Override
    public void run() {
        while(true){

            List<Order> orderList = mongoDBRepository.getAll();
            long sleepTime = 30;
            for(Order order : orderList){
                long minutes = ChronoUnit.MINUTES.between(order.getOrderTime().minusHours(3), LocalDateTime.now());

                if(minutes >= 30 && order.getProccessingTime() == null){
                    mongoDBRepository.updateOrder(order.getOrderId(), "failed");
                }else{
                    if(minutes < sleepTime){
                        sleepTime = 30 - minutes;
                    }
                }
            }

            try {
                Thread.sleep(sleepTime * 1000 * 60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
