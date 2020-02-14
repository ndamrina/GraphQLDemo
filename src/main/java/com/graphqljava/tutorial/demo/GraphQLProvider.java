package com.graphqljava.tutorial.demo;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

@Component
public class GraphQLProvider {

    private GraphQLDataFetchers graphQLDataFetchers;
    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    public GraphQLProvider(GraphQLDataFetchers graphQLDataFetchers) {
        this.graphQLDataFetchers = graphQLDataFetchers;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                            .type(newTypeWiring("Query")
                                .dataFetcher("getAllFlights", graphQLDataFetchers.getAllFlights())
                                .dataFetcher("getFlightById", graphQLDataFetchers.getFlightById()))

                            .type(newTypeWiring("Mutation")
                                      .dataFetcher("createFlight", graphQLDataFetchers.addFlight()))
                            .scalar(ExtendedScalars.Date)
                            .scalar(ExtendedScalars.DateTime)
                            .scalar(ExtendedScalars.NonNegativeFloat)
                            .build();
    }
}
