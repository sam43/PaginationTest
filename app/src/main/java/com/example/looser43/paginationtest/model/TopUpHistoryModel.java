package com.example.looser43.paginationtest.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUpHistoryModel{

	@SerializedName("topuphis")
	@Expose
	private Topuphis topuphis;

	@SerializedName("success")
	@Expose
	private String success;

	@SerializedName("url")
	@Expose
	private String url;

	public void setTopuphis(Topuphis topuphis){
		this.topuphis = topuphis;
	}

	public Topuphis getTopuphis(){
		return topuphis;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"TopUpHistoryModel{" + 
			"topuphis = '" + topuphis + '\'' + 
			",success = '" + success + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}