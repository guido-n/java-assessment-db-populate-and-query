package com.guido.model.builder;

import com.guido.model.Order;

public class OrderBuilder {
    private int id;
    private String orderNumber;
    private int personId;
    private String personName;

    public OrderBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder withPersonId(int personId) {
        this.personId = personId;
        return this;
    }

    public OrderBuilder withPersonName(String personName) {
        this.personName = personName;
        return this;
    }

    public Order build() {
        Order o = new Order();
        o.setId(id);
        o.setOrderNumber(orderNumber);
        o.setPersonId(personId);
        o.setPersonName(personName);
        return o;
    }
}