package com.capegemini.cafe.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CafeBillServiceImpl.class);
	
	public CafeBillServiceImpl(){
		cafeMenu = new HashMap<String, MenuItem>();
		orderItem = new HashMap<String, Integer>();
	}
	
	@Override
	public void orderItem(String name, Integer quantity) {
		LOGGER.info("Start: orderItem() of  CafeBillServiceImpl");
		if(quantity !=0 ){
			orderItem.put(name, quantity);
		}
		LOGGER.info("End: orderItem() of  CafeBillServiceImpl");
	}

	@Override
	public void addMenuItem(String name, String menuType, Double price) {
		LOGGER.info("Start: addMenuItem() of  CafeBillServiceImpl");
		if(!price.equals(0.0)){
			MenuItem menuItem = new MenuItem(menuType, price);
			cafeMenu.put(name, menuItem);
		}
		LOGGER.info("End: addMenuItem() of  CafeBillServiceImpl");
	}
	
	@Override
	public Double calculateBill(){
		LOGGER.info("Start: calculateBill() of  CafeBillServiceImpl");
		
		Double totalPrice = 0.00;
		
		boolean serviceCharge = false;
		Double serviceChargePer = 0.10;
		Double serviceChargeAmount = 0.00;

		LOGGER.info("Itemised Bill");
		
		Iterator<String> orderItemIterator = orderItem.keySet().iterator();
		
		while(orderItemIterator.hasNext()){
			String item = orderItemIterator.next();
			Integer quantity = orderItem.get(item);
			
			Double itemTotalPrice = caclulateItemBill(item, quantity);
			
			totalPrice = totalPrice + itemTotalPrice;
			
			MenuItem menuItem = cafeMenu.get(item);
			
			LOGGER.info("Item = " + item + ", Quantity " + quantity + " * itemPrice " + menuItem.getPrice() + " = " + itemTotalPrice);
			
			//Service Charge Calculations
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
				LOGGER.info("Service Charge @20 GBP = " + HOT_FOOD_MAX_SC);
				totalPrice = totalPrice + HOT_FOOD_MAX_SC;
			}else{
				LOGGER.info("Service Charge @" + serviceChargePer + "% = " + serviceChargeAmount);
				totalPrice = totalPrice + serviceChargeAmount;	
			}
		}

		LOGGER.info("Total Amount = " + totalPrice);
		
		LOGGER.info("End: calculateBill() of  CafeBillServiceImpl");
		
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
