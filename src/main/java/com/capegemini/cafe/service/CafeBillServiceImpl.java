package com.capegemini.cafe.service;

import java.util.HashMap;
import java.util.Map;

import com.capegemini.cafe.model.MenuItem;

public class CafeBillServiceImpl implements CafeBillService {
	
	private Map<String, MenuItem> cafeMenu;

	private Map<String, Integer> orderItem;
	
	public CafeBillServiceImpl(){
		cafeMenu = new HashMap<String, MenuItem>();
		orderItem = new HashMap<String, Integer>();
	}
	
	@Override
	public void orderItem(String name, Integer quantity) {
		if(quantity !=0 ){
			orderItem.put(name, quantity);
		}
	}

	@Override
	public void addMenuItem(String name, String menuType, Double price) {
		if(!price.equals(0.0)){
			MenuItem menuItem = new MenuItem(menuType, price);
			cafeMenu.put(name, menuItem);
		}
	}
	
	@Override
	public Map<String, MenuItem> getCafeMenu() {
		return cafeMenu;
	}
	
	@Override
	public Map<String, Integer> getOrderItem() {
		return orderItem;
	}

}
