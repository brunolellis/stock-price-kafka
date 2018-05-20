package br.com.lellis.stockprice.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import br.com.lellis.stockprice.serialization.JsonConverter;
import br.com.lellis.stockprice.stock.StockPrice;

public class StockPriceProducer {

	public static final String TOPIC = "stock-price";
	public static final Object KAFKA_URL = "localhost:9092";
	
	private static StockPriceProducer INSTANCE;
	
	private KafkaProducer<Long, String> producer;

	private JsonConverter converter = new JsonConverter();

	private StockPriceProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_URL);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "stock-price-producer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		producer = new KafkaProducer<>(props);
	}

	public static StockPriceProducer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new StockPriceProducer();
		}
		
		return INSTANCE;
	}

	public void send(StockPrice stock) {
		var msg = converter.toJson(stock);
		var record = new ProducerRecord<>(TOPIC, System.currentTimeMillis(), msg);
		
		producer.send(record);
	}

	public void close() {
		producer.flush();
		producer.close();
	}

}
