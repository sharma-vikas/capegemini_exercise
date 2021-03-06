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
		
		//Items Bill 5 + Service Charge@10%(Food item) .5 = 5.5
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("5.5", billAmount.toString());
		
	}
	
	@Test
	public void calculateBillHotFoodServiceChargeTest(){
		cafeBillService.orderItem(COLA, 2);
		cafeBillService.orderItem(COFFEE, 2);
		cafeBillService.orderItem(STEAK_SANDWICH, 1);
		
		//Items Bill 7.5 + Service Charge@20%(Hot Food item) 1.5 = 9.0
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("9.0", billAmount.toString());
		
	}
	
	@Test
	public void calculateBillHotFoodServiceChargeTwoDecimalPlacesTest(){
		cafeBillService.orderItem(COLA, 7);
		cafeBillService.orderItem(COFFEE, 5);
		cafeBillService.orderItem(CHEESE_SANDWICH, 50);
		
		//Service Charge calculated as 10.850000000000001 but rounded to 10.85 
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("119.35", billAmount.toString());
		
	}
	
	@Test
	public void calculateBillHotFoodServiceChargeMaxAmountTest(){
		cafeBillService.orderItem(COLA, 7);
		cafeBillService.orderItem(COFFEE, 5);
		cafeBillService.orderItem(STEAK_SANDWICH, 5);
		cafeBillService.orderItem(CHEESE_SANDWICH, 50);
		
		//Food Bill 131 + Service Charge 20(initial calculation as 26.2) = 151 
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("151.0", billAmount.toString());
		
	}
	
	//Requirement only says to reduce the service charges to GBP 20 if order includes any Hot Food item.
	@Test
	public void calculateBillHotFoodServiceChargeNoMaxAmountTest(){
		cafeBillService.orderItem(COLA, 7);
		cafeBillService.orderItem(COFFEE, 50);
		cafeBillService.orderItem(CHEESE_SANDWICH, 100);
		
		//Food Bill 253.5 + Service Charge 25.35(No max service charge consideration as no hot food item in order) = 278.85 
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("278.85", billAmount.toString());
		
	}
	
	//Chosen Logger.Info over SysOut, No easy way to verify all logs got printed through test though verified manually, included all food item to ensure no breakage in logic
	@Test
	public void calculateBillLogsPrintTest(){
		cafeBillService.orderItem(COLA, 7);
		cafeBillService.orderItem(COFFEE, 50);
		cafeBillService.orderItem(CHEESE_SANDWICH, 100);
		cafeBillService.orderItem(STEAK_SANDWICH, 50);
		
		//Food Bill 478.5 + Service Charge 20(No max service charge consideration as no hot food item in order) = 498.5 
		Double billAmount = cafeBillService.calculateBill();
		Assert.assertEquals("498.5", billAmount.toString());
		
	}
}
