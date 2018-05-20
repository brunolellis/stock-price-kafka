package br.com.lellis.stockprice.consumer;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import br.com.lellis.stockprice.producer.StockPriceProducer;

//public class StockPriceConsumer {
//	
//	  private static Consumer<Long, String> createConsumer() {
//	      final Properties props = new Properties();
//	      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, StockPriceProducer.TOPIC);
//	      props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
//	      props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
//	      props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//
//	      // Create the consumer using props.
//	      final Consumer<Long, String> consumer =
//	                                  new KafkaConsumer<>(props);
//
//	      // Subscribe to the topic.
//	      consumer.subscribe(Collections.singletonList(TOPIC));
//	      return consumer;
//	  }
//	
//	public void test() {
//        final Consumer<Long, String> consumer = createConsumer();
//
//        final int giveUp = 100;   int noRecordsCount = 0;
//
//        while (true) {
//            final ConsumerRecords<Long, String> consumerRecords =
//                    consumer.poll(1000);
//
//            if (consumerRecords.count()==0) {
//                noRecordsCount++;
//                if (noRecordsCount > giveUp) break;
//                else continue;
//            }
//
//            consumerRecords.forEach(record -> {
//                System.out.printf("Consumer Record:(%d, %s, %d, %d)\n",
//                        record.key(), record.value(),
//                        record.partition(), record.offset());
//            });
//
//            consumer.commitAsync();
//        }
//        consumer.close();
//        System.out.println("DONE");
//		
//	}
//}

public class StockPriceConsumer implements Runnable {

	private final KafkaConsumer<Long, String> consumer;
	private final List<String> topics;
	private String id;

	public StockPriceConsumer(String id) {
		this.id = id;
		this.topics = List.of(StockPriceProducer.TOPIC);

		Properties props = new Properties();
		props.put("bootstrap.servers", StockPriceProducer.KAFKA_URL);
		props.put("group.id", "stock-price-consumer-" + id);
		props.put("key.deserializer", LongDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new KafkaConsumer<>(props);
	}

	@Override
	public void run() {
		try {
			consumer.subscribe(topics);
			System.out.println(id + " subscribed to " + topics);

			while (true) {
				var stockPrices = consumer.poll(1000);
				stockPrices.forEach(s -> {
					System.out.println(Thread.currentThread().getName() + ": id=" + id + ", offset=" + s.offset() + ", key=" + s.key() + ", value=" + s.value() + ", timestamp=" + s.timestamp());
				});
			}
		} catch (WakeupException e) {
			// ignore for shutdown
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public void shutdown() {
		consumer.wakeup();
	}
}
