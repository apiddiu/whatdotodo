package com.aldo.whatdotodo.controller.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class StatusSerializer extends JsonSerializer<Status> {

    @Override
    public void serialize(Status status, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider
    ) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(status.getName());
    }
}
