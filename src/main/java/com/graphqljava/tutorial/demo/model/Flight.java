package com.graphqljava.tutorial.demo.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
@Data
public class Flight {
    private String id;
    private String title;
    private OffsetDateTime dateTime;
    private City to;
    private City from;
    private List<Person> passengers;

    public Flight(String id, String title, OffsetDateTime dateTime, City to, City from) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.to = to;
        this.from = from;
        this.passengers = new ArrayList<>();
    }

    public Flight() {}
}
