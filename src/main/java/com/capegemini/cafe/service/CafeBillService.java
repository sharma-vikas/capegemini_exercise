package com.capegemini.cafe.service;

import java.util.Map;

import com.capegemini.cafe.model.MenuItem;

public interface CafeBillService {

	public void addMenuItem(String name, String menuType, Double price);
	
	public void orderItem(String name, Integer quantity);
	
	public Map<String, MenuItem> getCafeMenu();
	
	public Map<String, Integer> getOrderItem();
	
}
