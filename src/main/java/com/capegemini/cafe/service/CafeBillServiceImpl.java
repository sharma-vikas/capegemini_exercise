package com.capegemini.cafe.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.capegemini.cafe.exception.BillServiceException;
import com.capegemini.cafe.model.MenuItem;

public class CafeBillServiceImpl implements CafeBillService {
	
	private Map<String, MenuItem> cafeMenu;

	private Map<String, Integer> orderItem;
	
	private final static String INVALID_MENU_ITEM = "Invalid Menu Item";
	private final static String FOOD = "food";
	private final static String HOT_FOOD = "Hot food";
	private final static Double HOT_FOOD_PERC = 0.20;
	private final static Double HOT_FOOD_MAX_SC = 20.00;
	
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
	public Double calculateBill(){
		
		Double totalPrice = 0.00;
		
		boolean serviceCharge = false;
		Double serviceChargePer = 0.10;
		Double serviceChargeAmount = 0.00;
		
		Iterator<String> orderItemIterator = orderItem.keySet().iterator();
		
		while(orderItemIterator.hasNext()){
			String item = orderItemIterator.next();
			Integer quantity = orderItem.get(item);
			
			Double itemTotalPrice = caclulateItemBill(item, quantity);
			
			totalPrice = totalPrice + itemTotalPrice;
			
			MenuItem menuItem = cafeMenu.get(item);
			if(menuItem.getMenuType().contains(FOOD)){
				serviceCharge = true;
				if(menuItem.getMenuType().contains(HOT_FOOD)){
					serviceChargePer = HOT_FOOD_PERC;
				}
				
			}
		}
		
		if(serviceCharge){
			serviceChargeAmount = totalPrice * serviceChargePer;
			serviceChargeAmount = new BigDecimal(serviceChargeAmount.toString()).setScale(2,RoundingMode.HALF_UP).doubleValue();
			if(serviceChargePer.equals(HOT_FOOD_PERC) && serviceChargeAmount > HOT_FOOD_MAX_SC){
				totalPrice = totalPrice + HOT_FOOD_MAX_SC;
			}else{
				totalPrice = totalPrice + serviceChargeAmount;	
			}
			
		}
		
		return totalPrice;
	}
	
	private Double caclulateItemBill(String item, Integer quantity){
		
		MenuItem menuItem = cafeMenu.get(item);
		Double itemTotal = 0.0;
		if(null != menuItem){
			
			itemTotal = menuItem.getPrice() * quantity;
			
		}else{
			
			throw new BillServiceException(INVALID_MENU_ITEM);
		}
		
		return itemTotal;
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
