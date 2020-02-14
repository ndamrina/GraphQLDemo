package com.graphqljava.tutorial.demo;

import graphql.schema.DataFetcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.graphqljava.tutorial.demo.model.City;
import com.graphqljava.tutorial.demo.model.Flight;
import com.graphqljava.tutorial.demo.model.Person;


@Component

public class GraphQLDataFetchers {
    // Data can come from a static in memory list, from a database or an external service
    private ArrayList<Flight> flights;

    @PostConstruct
    private void init() {
        Person brad = new Person("person-1", "Brad", "Pitt", LocalDate.of(1963, 12, 18));
        Person abraham = new Person("person-2", "Abraham", "Lincoln", LocalDate.of(1809, 2, 12));
        Person marilyn = new Person("person-3", "Marilyn", "Monroe", LocalDate.of(1926, 6, 1));
        Person diana = new Person("person-4", "Diana", "Princess of Wales", LocalDate.of(1961, 7, 1));

        Flight flight1 = new Flight("flight-1", "Bad idea", OffsetDateTime.now(), City.WUTAN, City.NEW_YORK);
        Flight flight2 = new Flight("flight-2", "Impossible",
                                    OffsetDateTime.of(LocalDateTime.of(2020, 3, 1, 1, 1), ZoneOffset.ofHours(1)),
                                    City.NEW_YORK, City.WUTAN);
        Flight flight3 = new Flight("flight-3", "Quickest flight ever", OffsetDateTime.now(), City.NEW_YORK, City.NEW_YORK);

        flight1.setPassengers(Arrays.asList(brad, diana));
        flight2.setPassengers(Arrays.asList(abraham, marilyn));

        this.flights = new ArrayList<>(Arrays.asList(flight1, flight2, flight3));
    }

    public DataFetcher<ArrayList<Flight>> getAllFlights() {
        return dataFetchingEnvironment -> flights;
    }

    public DataFetcher<Flight> getFlightById() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");

            return flights.stream().filter(f -> (f.getId().equals(id))).findFirst().orElse(null);
        };
    }

    public DataFetcher<Flight> addFlight() {
        return dataFetchingEnvironment -> {
            String title = dataFetchingEnvironment.getArgument("title");
            City to = City.valueOf(dataFetchingEnvironment.getArgument("to"));
            City from = City.valueOf(dataFetchingEnvironment.getArgument("from"));
            int num = flights.size();
            Flight flight = new Flight("flight-" + num, title, OffsetDateTime.now(), to, from);

            flights.add(flight);

            return flight;
        };
    }

}
