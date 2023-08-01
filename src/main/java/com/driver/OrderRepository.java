package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnersDb = new HashMap<>();
    HashMap<String, String> order_partners_db = new HashMap<>();
    HashMap<String, List<String>> partners_order_list = new HashMap<>();


    public void addOrder(Order order) {

        orderDb.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        partnersDb.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        order_partners_db.put(orderId, partnerId);
        List<String> ordersList = partners_order_list.getOrDefault(partnerId, new ArrayList<>());
        ordersList.add(orderId);
        partners_order_list.put(partnerId, ordersList);
        DeliveryPartner dp = partnersDb.get(partnerId);
        dp.setNumberOfOrders(dp.getNumberOfOrders()+1);
        partnersDb.put(partnerId, dp);
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnersDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(partners_order_list.containsKey(partnerId)) {
            return partners_order_list.get(partnerId).size();
        }

        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        if(partners_order_list.containsKey(partnerId))
            return partners_order_list.get(partnerId);

        return new ArrayList<>();
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for(String orderId : orderDb.keySet())
            orders.add(orderId);

        return orders;

    }

    public Integer getCountOfUnassignedOrders() {
        Integer count = 0;
        for(String orderId : orderDb.keySet()) {
            if(!order_partners_db.containsKey(orderId))
                count++;
        }

        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int time, String partnerId) {
        Integer count = 0;
        for(String orderId : partners_order_list.get(partnerId)) {
            if(orderDb.get(orderId).getDeliveryTime() > time)
                count++;
        }

        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int time = 0;
        for(String orderId : partners_order_list.get(partnerId)) {
            int curr_time = orderDb.get(orderId).getDeliveryTime();
            if(curr_time > time)
                time = curr_time;
        }
        return time;
    }

    public void deletePartnerById(String partnerId) {
        partnersDb.remove(partnerId);
        if(partners_order_list.containsKey(partnerId)) {
            for (String order : partners_order_list.get(partnerId)) {
                order_partners_db.remove(order);
            }
            partners_order_list.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId) {
        orderDb.remove(orderId);
        if(order_partners_db.containsKey(orderId)) {
            partners_order_list.get(order_partners_db.get(orderId)).remove(orderId);
            order_partners_db.remove(orderId);
        }
    }

    public void printAllDbs() {
        for(String id : orderDb.keySet())
            System.out.println(id + " " + orderDb.get(id));

        for(String id : partnersDb.keySet())
            System.out.println(id + " " + partnersDb.get(id));

        for(String id : order_partners_db.keySet())
            System.out.println(id + " " + order_partners_db.get(id));

        for(String id : partnersDb.keySet()) {
            for (String order : partners_order_list.get(id))
                System.out.print(order + " ,");
            System.out.println();
        }
    }
}
