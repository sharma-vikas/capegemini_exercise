package com.capegemini.cafe;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capegemini.cafe.exception.BillServiceException;
import com.capegemini.cafe.service.CafeBillService;
import com.capegemini.cafe.service.CafeBillServiceImpl;


public class CafeBillServiceTest {

	private CafeBillService cafeBillService;
	
	private final static String COLA = "Cola";
	private final static String COLD_Drink = "Cold  drink";
	private final static String COFFEE = "Coffee";
	private final static String HOT_Drink = "Hot  drink";
	private final static String CHEESE_SANDWICH = "Cheese Sandwich";
	private final static String COLD_FOOD = "Cold food";
	private final static String STEAK_SANDWICH = "Steak Sandwich";
	private final static String HOT_FOOD = "Hot food";
	private final static String NEW_ITEM = "New item";
	private final static String FOOD_TYPE = "Food type";
	private final static String INVALID_MENU_ITEM = "Invalid Menu Item";
	
	@Before
	public void setUp() throws Exception {
		
		cafeBillService = new CafeBillServiceImpl();

		cafeBillService.addMenuItem(COLA, COLD_Drink, .50);
		cafeBillService.addMenuItem(COFFEE, HOT_Drink, 1.00);
		cafeBillService.addMenuItem(CHEESE_SANDWICH, COLD_FOOD, 2.00);
		cafeBillService.addMenuItem(STEAK_SANDWICH, HOT_FOOD, 4.50);
	}
	
	@Test
	public void addMenuItemTest(){
		
		int cafeMenuSize = cafeBillService.getCafeMenu().size();
		cafeBillService.addMenuItem(NEW_ITEM,FOOD_TYPE,1.00);
		
		Assert.assertEquals(cafeMenuSize+1, cafeBillService.getCafeMenu().size());
		Assert.assertNotNull(cafeBillService.getCafeMenu().get(NEW_ITEM));
	}
	
	@Test
	public void addMenuItemTestFailure(){
		
		int cafeMenuSize = cafeBillService.getCafeMenu().size();
		cafeBillService.addMenuItem(NEW_ITEM,FOOD_TYPE,0.00);
		
		Assert.assertEquals(cafeMenuSize, cafeBillService.getCafeMenu().size());
		Assert.assertNull(cafeBillService.getCafeMenu().get(NEW_ITEM));
	}
	
	@Test
	public void orderItemTest(){
		
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(CHEESE_SANDWICH, 3);
		
		Assert.assertEquals(2, cafeBillService.getOrderItem().size());
		Assert.assertNotNull(cafeBillService.getOrderItem().get(CHEESE_SANDWICH));
		
	}
	
	@Test
	public void orderItemTestFailure(){
		
		cafeBillService.orderItem(COLA, 0);
		
		Assert.assertEquals(0, cafeBillService.getOrderItem().size());
		Assert.assertNull(cafeBillService.getOrderItem().get(COLA));
		
	}
	
	@Test
	public void calculateBillTest(){
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(COFFEE, 2);
		
		Double totalAmount = cafeBillService.calculateBill();
		
		Assert.assertEquals("3.0", totalAmount.toString());
	}
	
	@Test
	public void calculateBillTestFailure(){
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(COFFEE, 2);
		cafeBillService.orderItem(NEW_ITEM, 1);
		
		try{
			cafeBillService.calculateBill();
			Assert.fail("Control shouldn't reach here");
		}catch(BillServiceException bse){
			Assert.assertEquals(INVALID_MENU_ITEM, bse.getMessage());
		}
		
	}
	
	@Test
	public void calculateBillServiceChargeTest(){
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(COFFEE, 2);
		cafeBillService.orderItem(CHEESE_SANDWICH, 1);
		
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("5.5", billAmount.toString());
		
	}
	
	@Test
	public void calculateBillHotFoodServiceChargeTest(){
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(COFFEE, 2);
		cafeBillService.orderItem(STEAK_SANDWICH, 1);
		
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("9.0", billAmount.toString());
		
	}
	
	@Test
	public void calculateBillHotFoodServiceChargeTwoDecimalPlacesTest(){
		cafeBillService.orderItem(COLA, 7);
		cafeBillService.orderItem(COFFEE, 5);
		cafeBillService.orderItem(STEAK_SANDWICH, 3);
		cafeBillService.orderItem(CHEESE_SANDWICH, 5);
		
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("38.4", billAmount.toString());
		
	}
}
