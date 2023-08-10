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
        List<String> list = partners_order_list.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        partners_order_list.put(partnerId, list);
        order_partners_db .put(orderId, partnerId);
        DeliveryPartner partner = partnersDb.get(partnerId);
        partner.setNumberOfOrders(list.size());
//        if(orderDb.containsKey(orderId) && partnersDb.containsKey(partnerId)) {
//            order_partners_db.put(orderId, partnerId);
//
//            List<String> ordersList = new ArrayList<>();
//            if(partners_order_list.containsKey(partnerId)) {
//                ordersList = partners_order_list.get(partnerId);
//            }
//
//            ordersList.add(orderId);
//            partners_order_list.put(partnerId, ordersList);
//
//            DeliveryPartner dp = partnersDb.get(partnerId);
////            dp.setNumberOfOrders(dp.getNumberOfOrders() + 1);
//            dp.setNumberOfOrders(ordersList.size());
//        }
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnersDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
//        if(partners_order_list.containsKey(partnerId)) {
//            return partners_order_list.get(partnerId).size();
//        }

        return partnersDb.get(partnerId).getNumberOfOrders();
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
//        Integer count = 0;
//        for(String orderId : orderDb.keySet()) {
//            if(!order_partners_db.containsKey(orderId))
//                count++;
//        }

        return orderDb.size()-order_partners_db.size();
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
            String partner = order_partners_db.get(orderId);
            partners_order_list.get(partner).remove(orderId);
            order_partners_db.remove(orderId);
            partnersDb.get(partner).setNumberOfOrders(partners_order_list.get(partner).size());
        }
    }

}
