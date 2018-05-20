package br.com.lellis.stockprice;

import java.math.BigDecimal;
import java.util.List;

import br.com.lellis.stockprice.producer.StockPriceProducer;
import br.com.lellis.stockprice.stock.StockPrice;

/**
 * 
 * IMPROVEMENTS: 
 * 
 * - ExecutorService to handle multi thread context 
 * - this project is just a "hello world" in kafka ecosystem, so I am using jackson to
 * simplify the serialization of a simple POJO to json and reading it back; 
 * - an option might be to use another serializer, something like avro or protocol buffer?
 * 
 */
public class MainAppStockPriceProducer {

	public static void main(String[] args) {
		var bovespa = List.of("PETR4", "ITSA4", "BBAS3", "CIEL3");
		
		var stockProducer = StockPriceProducer.getInstance();
		
		for (int i = 0; i < 1000; i++) {
			bovespa.parallelStream()
				.forEach(ticker -> {
					var stock = new StockPrice(ticker, randomPrice());
					stockProducer.send(stock);
				});
		}
		
		stockProducer.close();
	}

	private static BigDecimal randomPrice() {
		return new BigDecimal(random(1, 200));
	}

	private static int random(int min, int max) {
		int range = (max - min) + 1;
		return (int) (Math.random() * range) + min;
	}

}
