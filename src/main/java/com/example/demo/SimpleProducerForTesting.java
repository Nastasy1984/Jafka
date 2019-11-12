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
	private Producer<String, MyModel> producer;

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
     public Producer<String, MyModel> producer (){
		return new KafkaProducer<>(props);
	}
	
	public void sendProbeMessages() {
		//sending first probe message
		MyModel mmModel = new MyModel();
		mmModel.setMessageString("HELLO!!!");
        producer.send(new ProducerRecord<String, MyModel>(initialKafkaTopic, mmModel));
        MyModel model = new MyModel();
		for (int i = 0; i < 10; i++) {
			model.setMessageString("TEST MESSAGE NUMBER " + i);
			producer.send(new ProducerRecord<String, MyModel>(initialKafkaTopic, model));
		}
		producer.close();
	}


}
