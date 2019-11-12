package com.example.demo;

import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
public class MySerializer extends JsonSerializer<MyModelWithTimestamp>{
}
