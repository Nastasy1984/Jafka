package com.example.demo;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.producer.ProducerRecord;



@Configuration
@ConfigurationProperties("kafka")
public class SimpleProducerForTesting {
  
	@Value("${kafka.topic.initial-topic}")
	private String initialKafkaTopic;

	@Value("${kafka.bootstrap-servers}")
	private String HOST;
	
	@Autowired
	@Qualifier("myProps")
	private Properties props;
	
	@Autowired
	private Producer<String, String> producer;

	@Bean (name="myProps")
	public Properties props() {
		// creating properties for producer
		Properties props = new Properties();
		props.put("bootstrap.servers", HOST);
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		return props;
	}
	
	@Bean
     //creating producer using properties
     public Producer<String, String> producer (){
		return new KafkaProducer<>(props);
	}
	
	public void sendProbeMessages() {
		//sending first probe message
		String probeMessageString = "{\"message\": \"HELLO\"}";
        producer.send(new ProducerRecord<String, String>(initialKafkaTopic, probeMessageString));

		for (int i = 0; i < 3; i++) {
			probeMessageString = "{\"message\": \"HELLO" + " " + i + "\"}";
			producer.send(new ProducerRecord<String, String>(initialKafkaTopic, probeMessageString));
		}
		
		probeMessageString = "{\"field1\": \"aaa\", \"field2\": 123, \"Timestamp\": 12312413252534}";
		producer.send(new ProducerRecord<String, String>(initialKafkaTopic, probeMessageString));
		producer.close();
	}


}
