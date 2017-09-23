package com.example.looser43.paginationtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("packlogo")
	@Expose
	private String packlogo;

	@SerializedName("amount")
	@Expose
	private String amount;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("created_at")
	@Expose
	private String createdAt;

	@SerializedName("type")
	@Expose
	private String type;

	@SerializedName("day")
	@Expose
	private Object day;

	@SerializedName("status")
	@Expose
	private String status;

	public void setPacklogo(String packlogo){
		this.packlogo = packlogo;
	}

	public String getPacklogo(){
		return packlogo;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setDay(Object day){
		this.day = day;
	}

	public Object getDay(){
		return day;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"packlogo = '" + packlogo + '\'' + 
			",amount = '" + amount + '\'' + 
			",name = '" + name + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",type = '" + type + '\'' + 
			",day = '" + day + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}