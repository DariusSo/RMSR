package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MongoDBService {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDBService() {
        // Connect to MongoDB (default localhost:27017)
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
        order.setDishes(doc.getList("dishes", String.class));
        order.setOrderTime(doc.get("orderTime", LocalDateTime.class));
        order.setProccessingTime(doc.get("proccessingTime", LocalDateTime.class));
        System.out.println("Rado: " + order.getOrderId());
        return order;
    }

    private Document orderToDocument(Order order) {
        return new Document("orderId", order.getOrderId())
                .append("client", order.getClient())
                .append("table", order.getTable())
                .append("dishes", order.getDishes())
                .append("status", order.getStatus())
                .append("orderTime", order.getOrderTime())
                ;
    }

    public void addOrder(Order order) {
        Document doc = orderToDocument(order);
        collection.insertOne(doc);
        order.setId(doc.getObjectId("_id").toString());  // set generated ID
        System.out.println("Added order: " + order.getOrderId());
    }
//
//    public List<Person> getAllPersons() {
//        List<Person> people = new ArrayList<>();
//        MongoCursor<Document> cursor = collection.find().iterator();
//        try {
//            while (cursor.hasNext()) {
//                Document doc = cursor.next();
//                people.add(documentToPerson(doc));
//            }
//        } finally {
//            cursor.close();
//        }
//        return people;
//    }
//
    public Order getOrderById(String id) {
        Document doc = collection.find(Filters.eq("orderId", id)).first();
        return doc != null ? documentToOrder(doc) : null;
    }

    public void updateOrder(String id){
        Document doc = collection.find(Filters.eq("orderId", id)).first();
        Order order = documentToOrder(doc);
        Document newDocument = new Document();
        newDocument.append(order.getId(), "_id");
        newDocument.append(order.getOrderId(), "orderId");
        newDocument.append(order.getClient(), "client");
        newDocument.append(order.getTable(), "table");


//        doc.append("status", "ready");
//        collection.updateOne(doc, doc);
    }
//
//    public Person getPersonByName(String personName) {
//        Document doc = collection.find(Filters.eq("name", personName)).first();
//        return doc != null ? documentToPerson(doc) : null;
//    }
//
//    public void updatePerson(String id, Person person) {
//        Document updatedDoc = personToDocument(person);
//        collection.updateOne(Filters.eq("_id", new ObjectId(id)), new Document("$set", updatedDoc));
//        System.out.println("Updated person with id: " + id);
//    }
//
//    public void deletePerson(String id) {
//        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
//        System.out.println("Deleted person with id: " + id);
//    }

    public void close() {
        mongoClient.close();
    }
}
