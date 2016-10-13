package com.capegemini.cafe.exception;

import java.util.HashMap;
import java.util.Map;

public class BillServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public BillServiceException() {
		super();
	}

	public BillServiceException(String message) {
		super(message);
	}

	public BillServiceException(Throwable cause) {
		super(cause);
	}

	public BillServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		this.properties = properties;
	}
	
	public Object getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, Object value) {
		this.properties.put(name, value);
	}

	
}
