package com.example.demo;

import java.sql.Timestamp;
import java.time.Instant;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
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
@EnableIntegration
public class AppConfig {
	
	@Value("${kafka.topic.initial-topic}")
	private String initialKafkaTopic;

	@Value("${kafka.topic.new-topic}")
	private String newKafkaTopic;
	
	@Bean
	public IntegrationFlow fromKafkaFlow(ConsumerFactory<String, String> consumerFactory) {
		return IntegrationFlows.from(Kafka.messageDrivenChannelAdapter(consumerFactory, initialKafkaTopic))
				.<MyModelWithTimestamp, MyModelWithTimestamp>transform((p) -> {
					p.setTimestamp(Timestamp.from(Instant.now()).toString());
					return p;
				})
				// sending output to the direct channel myChannel
				.channel("myChannel").get();
	}

	@Bean
	public IntegrationFlow outFlow(KafkaTemplate<String, MyModelWithTimestamp> kafkaTemplate) {
		return IntegrationFlows.from("myChannel")
				.handle(Kafka.outboundChannelAdapter(kafkaTemplate).topic(newKafkaTopic)).
				get();
	}

	//creating new topic for inbound flow
	@Bean
	public NewTopic topic() {
		return new NewTopic(newKafkaTopic, 1, (short) 1);
	}
	
	//creating new topic for outbound flow
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
	
}
