package com.example.demo;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
public class MySerializer extends JsonSerializer<HashMap<String, String>>{
}
