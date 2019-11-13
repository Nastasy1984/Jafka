package com.example.demo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ConfigurationProperties("kafka")
@EnableKafka
public class AppConfig {
	
	@Value("${kafka.topic.initial-topic}")
	private String initialKafkaTopic;

	@Value("${kafka.topic.new-topic}")
	private String newKafkaTopic;

	@Bean
	public IntegrationFlow fromKafkaFlow(ConsumerFactory<String, Object> consumerFactory) {
		return IntegrationFlows.from(Kafka.messageDrivenChannelAdapter(consumerFactory, initialKafkaTopic))
				//.<HashMap<String, Object>, HashMap<String, Object>>transform((p) -> {
					//p.put("Timestamp", Timestamp.from(Instant.now()).toString());
					//return p;
				//})
				.<HashMap<String, Object>>handle((p, h) -> {
					p.put("Timestamp", Timestamp.from(Instant.now()).toString());
					return p;
				})
				// sending output to the direct channel myChannel
				.channel("myChannel").get();
	}

	@Bean
	public IntegrationFlow outFlow(KafkaTemplate<String, HashMap<String, Object>> kafkaTemplate) {
		return IntegrationFlows.from("myChannel")
				.handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic(newKafkaTopic)).
				get();
	}

	//creating new topic for outbound flow
	@Bean
	public NewTopic topic() {
		return new NewTopic(newKafkaTopic, 1, (short) 1);
	}
	
	//creating new topic for inbound flow
	@Bean
	public NewTopic initialTopic() {
		return new NewTopic(initialKafkaTopic, 1, (short) 1);
	}
	
	// channel from integration flow
	@Bean
	public MessageChannel myChannel() {
		DirectChannel directChannel = new DirectChannel();
		return directChannel;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
	
	//just for probe
	/*
	@Bean
	public IntegrationFlow fromKafkaFlow(ConsumerFactory<String, String> consumerFactory) {
		return IntegrationFlows.from(Kafka.messageDrivenChannelAdapter(consumerFactory, initialKafkaTopic))
				// .<String, String>transform(String::toUpperCase)
				// .<String, String>transform((p) -> p + "YYY")
				// .<String>handle((p, h) -> p + "VAS")
				//.<String>handle((p, h) -> p + Timestamp.from(Instant.now()))
				//deleting the last "}" and adding field with timestamp
				.<String>handle((p, h) -> {
					String toAdd = ", \"timestamp\": " + Timestamp.from(Instant.now()) + "}";
					return p.substring(0, p.length()) + toAdd;
				})
				.<String, Integer>transform((p) -> {
					return new Integer(5);
				})
				//получаем объект типа MyModel переводим его в объект MyModelWithTimestamp беря сообщение из старого и добавляя поле TimeStamp
				.<MyModel, MyModelWithTimestamp>transform((p) -> {
					MyModelWithTimestamp mm= new MyModelWithTimestamp();
					mm.setMessageString(p.getMessageString());
					mm.setTimestampString(Timestamp.from(Instant.now()).toString());
					return mm;
				})
				
				// sending output to the direct channel myChannel
				.channel("myChannel").get();
	}*/

}
