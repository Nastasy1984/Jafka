# Jafka
Spring-Kafka-Integration-Probe

Задание:
Develop Spring Boot application which consumes message (in JSON format) from topic in Apache Kafka, 
appends payload of that message with handling timestamp and output result as JSON in another Kafka topic. 
Use Spring Integration, Spring Kafka projects.

Пример отправляемых сообщений:
{“field1”: “aaa”, “field2”: 123}
{"name": "John", "age": 30, "isAdmin": false, "courses": ["html", "css", "js"], "wife": null}

Пример сообщения на выходе:
{"field1":"aaa","field2":123,"Timestamp":"2019-11-13 09:55:47.002"}

Навигация:
1) Класс Jafka1Application - запуск приложения (main)
2) Класс AppConfig - здесь все необходимые бины, формируются потоки для получения/обработки/отправки сообщений
3) Классы MySerializer и MyDeserializer - служебные классы, чтобы определить параметризированный тип сериализаторы/десериализатора
4) Класс SimpleProducerForTesting - простой (не спринговый) продюсер для тестирования работы приложения - в начале
работы программы отправляет несколько сообщений в topic
5) Файл application.yml - общие properties, необходимые для конфигурации (адрес кафка-сервера, сериализаторы, группа, topics)
