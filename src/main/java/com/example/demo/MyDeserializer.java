package com.example.demo;

import java.util.HashMap;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

@Component
public class MyDeserializer extends JsonDeserializer<HashMap<String, String>>{
}
