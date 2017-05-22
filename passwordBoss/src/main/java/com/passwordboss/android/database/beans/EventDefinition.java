package com.passwordboss.android.database.beans;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.passwordboss.android.database.DatabaseConstants;

@DatabaseTable(tableName = DatabaseConstants.EVENTS_DEFINITION_TABLE_NAME)
public class EventDefinition {

	@DatabaseField(
			columnName = DatabaseConstants.EVENT_DEF_ID, 
			dataType = DataType.STRING)
	private String event_def_id;
	
	@DatabaseField(columnName = DatabaseConstants.UUID, 
			dataType = DataType.STRING)
	private String uuid;
	
	@DatabaseField(
			columnName = DatabaseConstants.SECTION_ID,
			dataType = DataType.STRING)
	private String section_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.SECTION_NAME,
			dataType = DataType.STRING)
	private String section_name;

	@DatabaseField(
			columnName = DatabaseConstants.EVENT_ID,
			dataType = DataType.STRING)
	private String event_id;
	
	@DatabaseField(
			columnName = DatabaseConstants.EVENT_NAME,
			dataType = DataType.STRING)
	private String event_name;
	
	@DatabaseField(
			columnName = DatabaseConstants.COLLECT,
			dataType = DataType.STRING)
	private String collect;
	
	@DatabaseField(
			columnName = DatabaseConstants.COLLECT_ONCE,
			dataType = DataType.STRING)
	private String collect_once;
	
	@DatabaseField(columnName = DatabaseConstants.CREATED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String created_date;
	
	@DatabaseField(columnName = DatabaseConstants.LAST_MODIFIED_DATE, 
			dataType = DataType.STRING, canBeNull = false)
	private String last_modified_date;

	public String getEventDefId() {
		return event_def_id;
	}

	public void setEventDefId(String event_def_id) {
		this.event_def_id = event_def_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSectionId() {
		return section_id;
	}

	public void setSectionId(String section_id) {
		this.section_id = section_id;
	}

	public String getSectionName() {
		return section_name;
	}

	public void setSectionName(String section_name) {
		this.section_name = section_name;
	}

	public String getEventId() {
		return event_id;
	}

	public void setEventId(String event_id) {
		this.event_id = event_id;
	}

	public String getEventName() {
		return event_name;
	}

	public void setEventName(String event_name) {
		this.event_name = event_name;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public String getCollectOnce() {
		return collect_once;
	}

	public void setCollectOnce(String collect_once) {
		this.collect_once = collect_once;
	}

	public String getCreatedDate() {
		return created_date;
	}

	public void setCreatedDate(String created_date) {
		this.created_date = created_date;
	}

	public String getLastModifiedDate() {
		return last_modified_date;
	}

	public void setLastModifiedDate(String last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
}
