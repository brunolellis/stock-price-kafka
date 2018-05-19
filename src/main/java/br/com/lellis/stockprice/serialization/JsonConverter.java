package br.com.lellis.stockprice.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	private static final ObjectMapper mapper = new ObjectMapper();
	
	public <T> T fromJson(String src, Class<T> clazz) {
		try {
			return mapper.readValue(src, clazz);
			
		} catch (Exception e) {
			//log.error("error reading {}", src, e);
			throw new RuntimeException(e);
		}
	}
	
	public String toJson(Object value) {
		try {
			return mapper.writeValueAsString(value);
			
		} catch (Exception e) {
			//log.error("error writing {}", String.valueOf(value), e);
			throw new RuntimeException(e);
		}
	}
	
}
