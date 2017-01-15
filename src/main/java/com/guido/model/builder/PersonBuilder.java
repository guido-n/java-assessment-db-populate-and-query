package com.guido.model.builder;

import com.guido.model.Person;

public class PersonBuilder {
    private int id;
    private String firstName;
    private String lastName;
    private String street;
    private String city;

    public PersonBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PersonBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PersonBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder withStreet(String street) {
        this.street = street;
        return this;
    }

    public PersonBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public Person build() {
        Person p = new Person();
        p.setId(id);
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setCity(city);
        p.setStreet(street);
        return p;
    }
}