package com.graphqljava.tutorial.demo.model;

import lombok.Data;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
@Data
public class Person {
    private String id;
    private String first;
    private String last;
    private LocalDate birthday;

    public Person(String id, String first, String last, LocalDate birthday) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.birthday = birthday;
    }

    public Person() {}
}
