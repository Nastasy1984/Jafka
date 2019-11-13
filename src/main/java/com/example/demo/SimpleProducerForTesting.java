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
	private Producer<String, MyModelWithTimestamp> producer;

	@Bean (name="myProps")
	public Properties props() {
		// creating properties for producer
		Properties props = new Properties();
		props.put("bootstrap.servers", HOST);
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "com.example.demo.MySerializer");
		return props;
	}
	
	@Bean
     //creating producer using properties
     public Producer<String, MyModelWithTimestamp> producer (){
		return new KafkaProducer<>(props);
	}
	
	public void sendProbeMessages() {
		//sending first probe message
		MyModelWithTimestamp mmModel = new MyModelWithTimestamp();
		mmModel.setMessage("HELLO!!!");
        producer.send(new ProducerRecord<String, MyModelWithTimestamp>(initialKafkaTopic, mmModel));
        MyModelWithTimestamp model = new MyModelWithTimestamp();
		for (int i = 0; i < 10; i++) {
			model.setMessage("TEST MESSAGE NUMBER " + i);
			producer.send(new ProducerRecord<String, MyModelWithTimestamp>(initialKafkaTopic, model));
		}
		producer.close();
	}


}