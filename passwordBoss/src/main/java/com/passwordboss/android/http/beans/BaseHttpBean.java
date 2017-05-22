package com.passwordboss.android.http.beans;

import java.io.Serializable;

import org.joda.time.DateTime;

public class BaseHttpBean implements Serializable{
	private String code;
	private DateTime created_date;
	private DateTime last_modified_date;
	private String name;
	private int order;
	private String uuid;
	private boolean active;
	
	public BaseHttpBean() {
		super();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public DateTime getCreatedDate() {
		return created_date;
	}
	public void setCreatedDate(DateTime created_date) {
		this.created_date = created_date;
	}
	public DateTime getLastModifiedDate() {
		return last_modified_date;
	}
	public void setLastModifiedDate(DateTime last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
