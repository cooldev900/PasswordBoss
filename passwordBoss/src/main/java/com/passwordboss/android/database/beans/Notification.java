package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.NOTIFICATION_TABLE_NAME)
public class Notification extends Base {

	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_DATE, 
			dataType = DataType.STRING)
	private String notification_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_DISSMISSAL_DATE, 
			dataType = DataType.STRING)
	private String dismissal_date;
	
	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_DEVICE, 
			dataType = DataType.INTEGER)
	private int device;
	
	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_CATEGORY, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.ID)
	private NotificationCategory notification_category;
	
	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_DISPLAY, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.ID)
	private NotificationDisplay notification_display;
	
	@DatabaseField(
			columnName = DatabaseConstants.NOTIFICATION_SITE, 
			foreign = true, 
			foreignAutoRefresh = true, 
			foreignColumnName = DatabaseConstants.ID)
	private Site site;
	
	@DatabaseField(
			columnName = DatabaseConstants.TITLE, 
			dataType = DataType.STRING)
	private String title;
	
	@DatabaseField(
			columnName = DatabaseConstants.BODY, 
			dataType = DataType.STRING)
	private String body;
	
	
	public Notification() {
		
	}


	public String getNotificationDate() {
		return notification_date;
	}


	public void setNotificationDate(String notification_date) {
		this.notification_date = notification_date;
	}


	public String getDismissalDate() {
		return dismissal_date;
	}


	public void setDismissal_date(String dismissal_date) {
		this.dismissal_date = dismissal_date;
	}


	public int getDevice() {
		return device;
	}


	public void setDevice(int device) {
		this.device = device;
	}


	public NotificationCategory getNotificationCategory() {
		return notification_category;
	}


	public void setNotificationCategory(NotificationCategory notification_category) {
		this.notification_category = notification_category;
	}


	public NotificationDisplay getNotificationDisplay() {
		return notification_display;
	}


	public void setNotificationDisplay(NotificationDisplay notification_display) {
		this.notification_display = notification_display;
	}


	public Site getSite() {
		return site;
	}


	public void setSite(Site site) {
		this.site = site;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}
	
	
}
