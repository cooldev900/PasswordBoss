package com.passwordboss.android.http.beans;

import org.joda.time.DateTime;

public class SentHttpBean {

	private DateTime created_date, last_modified_date, expiration_date;
	private String hash, data, message,receiver, uuid, public_key, status, nickname;
	private int order;
	private SecureItemTypeHttpBean secure_item_type;
	private boolean visible, active, acknowledged;
	private SenderAccountHttpBean sender_account;
	
	public SenderAccountHttpBean getSenderAccount() {
		return sender_account;
	}
	public void setSenderAccount(SenderAccountHttpBean sender_account) {
		this.sender_account = sender_account;
	}
	public String getPublicKey() {
		return public_key;
	}
	public void setPublicKey(String public_key) {
		this.public_key = public_key;
	}
	public boolean isAcknowledged() {
		return acknowledged;
	}
	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	public DateTime getExpirationDate() {
		return expiration_date;
	}
	public void setExpirationDate(DateTime expirationDate) {
		this.expiration_date = expirationDate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public SecureItemTypeHttpBean getSecureItemType() {
		return secure_item_type;
	}
	public void setSecureItemType(SecureItemTypeHttpBean secure_item_type) {
		this.secure_item_type = secure_item_type;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}	
}
