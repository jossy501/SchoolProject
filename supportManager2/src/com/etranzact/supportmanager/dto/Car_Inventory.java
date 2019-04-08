package com.etranzact.supportmanager.dto;

public class Car_Inventory {
	
	private String counter;
	private String inventory_id;
	private String all_makes;
	private String all_model;
	private String car_year;
	private String engine;
	private String transmission;
	private String car_color;
	private String fuel_type;
	private String car_price;
	private String created_date;
	private String image_location;
	private String mileage;
	private String vin;
	private String stock_number;
	private String target_price;
	
	public Car_Inventory() {

	}
	public Car_Inventory(String counter, String inventory_id, String all_makes, String all_model, String car_year, String engine, String transmission,
			String car_color, String fuel_type, String car_price, String created_date, String image_location, String mileage, String vin,
			String stock_number, String target_price ) {
		this.counter = counter;
		this.inventory_id = inventory_id;
		this.all_makes = all_makes;
		this.all_model =all_model;
		this.car_year =car_year;
		this.engine = engine;
		this.transmission = transmission;
		this.car_color = car_color;
		this.fuel_type = fuel_type;
		this.car_price = car_price;
		this.created_date = created_date;
		this.vin = vin;
		this.stock_number = stock_number;
		this.target_price = target_price;
		
	}
	
	public String getCounter() {
		return counter;
	}
	public void setCounter(String counter) {
		this.counter = counter;
	}
	public String getInventory_id() {
		return inventory_id;
	}

	public void setInventory_id(String inventory_id) {
		this.inventory_id = inventory_id;
	}

	public String getAll_makes() {
		return all_makes;
	}

	public void setAll_makes(String all_makes) {
		this.all_makes = all_makes;
	}

	public String getAll_model() {
		return all_model;
	}

	public void setAll_model(String all_model) {
		this.all_model = all_model;
	}

	public String getCar_year() {
		return car_year;
	}

	public void setCar_year(String car_year) {
		this.car_year = car_year;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getCar_color() {
		return car_color;
	}

	public void setCar_color(String car_color) {
		this.car_color = car_color;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public String getCar_price() {
		return car_price;
	}

	public void setCar_price(String car_price) {
		this.car_price = car_price;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getImage_location() {
		return image_location;
	}

	public void setImage_location(String image_location) {
		this.image_location = image_location;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getStock_number() {
		return stock_number;
	}

	public void setStock_number(String stock_number) {
		this.stock_number = stock_number;
	}

	public String getTarget_price() {
		return target_price;
	}

	public void setTarget_price(String target_price) {
		this.target_price = target_price;
	}
	

	@Override
	public String toString() {
		return "Car_Inventory [inventory_id=" + inventory_id + ", all_makes=" + all_makes + ", all_model=" + all_model
				+ ", car_year=" + car_year + ", engine=" + engine + ", transmission=" + transmission + ", car_color="
				+ car_color + ", fuel_type=" + fuel_type + ", car_price=" + car_price + ", created_date=" + created_date
				+ ", image_location=" + image_location + ", mileage=" + mileage + ", vin=" + vin + ", stock_number="
				+ stock_number + ", target_price=" + target_price + ", getInventory_id()=" + getInventory_id()
				+ ", getAll_makes()=" + getAll_makes() + ", getAll_model()=" + getAll_model() + ", getCar_year()="
				+ getCar_year() + ", getEngine()=" + getEngine() + ", getTransmission()=" + getTransmission()
				+ ", getCar_color()=" + getCar_color() + ", getFuel_type()=" + getFuel_type() + ", getCar_price()="
				+ getCar_price() + ", getCreated_date()=" + getCreated_date() + ", getImage_location()="
				+ getImage_location() + ", getMileage()=" + getMileage() + ", getVin()=" + getVin()
				+ ", getStock_number()=" + getStock_number() + ", getTarget_price()=" + getTarget_price()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
	
	
	
	

}
