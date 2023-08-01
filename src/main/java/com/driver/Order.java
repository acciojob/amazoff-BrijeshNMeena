package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM

        this.id = id;

        deliveryTime.trim();
        int hour = Integer.parseInt(deliveryTime.substring(0, 2));
        int min = Integer.valueOf(deliveryTime.substring(2));

        this.deliveryTime = hour*60 + min;

    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}

//    @Override
//    public String toString() {
//        return "Order{" +
//                "id='" + id + '\'' +
//                ", deliveryTime=" + deliveryTime +
//                '}';
//    }
}
