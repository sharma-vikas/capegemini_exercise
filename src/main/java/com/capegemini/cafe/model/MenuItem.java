package com.capegemini.cafe.model;

public class MenuItem {

	private String menuType;
	private Double price;
	
	public MenuItem(String menuType, Double price) {
		this.menuType = menuType;
		this.price = price;
	}
	
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "MenuItem [menuType=" + menuType + ", price=" + price + "]";
	}
}
