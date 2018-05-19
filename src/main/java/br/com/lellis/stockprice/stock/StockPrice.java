package br.com.lellis.stockprice.stock;

import java.math.BigDecimal;

public class StockPrice {

	private String ticker; // PETR4.SA

	private BigDecimal price;

	// TODO: stop using BigDecimal in favor of double or separate values (real and decimal)?
	// private long real; // brazilian real
	// private long cents; // brazilian cents

	protected StockPrice() {
		// just for serializers
	}

	public StockPrice(String ticker, BigDecimal price) {
		this.ticker = ticker;
		this.price = price;
	}

	public String getTicker() {
		return ticker;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "StockPrice [ticker=" + ticker + ", price=" + price + "]";
	}

}
