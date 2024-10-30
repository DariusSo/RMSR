package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoDBRepository {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBRepository() {

        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("OrderDB");
        collection = database.getCollection("orders");
    }

    private Order documentToOrder(Document doc) {
        Order order = new Order();
        order.setId(doc.getObjectId("_id").toString());
        order.setOrderId(doc.getString("orderId"));
        order.setClient(doc.getString("client"));
        order.setTable(doc.getInteger("table"));
        order.setStatus(doc.getString("status"));
        order.setDishes(doc.getList("dishes", String.class));
        order.setOrderTime(convertToLocalDateTimeViaInstant(doc.getDate("orderTime")));
        order.setProccessingTime(convertToLocalDateTimeViaInstant(doc.getDate("proccessingTime")));

        return order;
    }

    private Document orderToDocument(Order order) {
        return new Document("orderId", order.getOrderId())
                .append("client", order.getClient())
                .append("table", order.getTable())
                .append("dishes", order.getDishes())
                .append("status", order.getStatus())
                .append("orderTime", order.getOrderTime())
                .append("paymentMethod", order.getPaymentMethod())
                .append("totalPrice", order.getTotalPrice())
                ;
    }

    public void addOrder(Order order) {
        Document doc = orderToDocument(order);
        collection.insertOne(doc);
        order.setId(doc.getObjectId("_id").toString());  // set generated ID
        System.out.println("Added order: " + order.getOrderId());
    }

    public void updateOrder(String id, String status){
        collection.updateOne(Filters.eq("orderId", id), Updates.set("status", status));
        if(status.equals("ready")){
            collection.updateOne(Filters.eq("orderId", id), Updates.set("proccessingTime", LocalDateTime.now()));
        }
    }

    public void acceptPayment(String id, String paymentMethod, double totalPrice){
        collection.updateOne(Filters.eq("orderId", id), Updates.set("paymentMethod", paymentMethod));
        collection.updateOne(Filters.eq("orderId", id), Updates.set("totalPrice", totalPrice));
    }

    public List<Order> getAll(){
        FindIterable<Document> doc = collection.find();
        List<Order> orderList = new ArrayList<>();
        for(Document d : doc){
            Order order = documentToOrder(d);
            orderList.add(order);
        }
        return orderList;
    }

    public void close() {
        mongoClient.close();
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        if(dateToConvert == null){
            return null;
        }
        return dateToConvert.toInstant()
                .atZone(ZoneId.of("Europe/Vilnius"))
                .toLocalDateTime();
    }
}
