package com.passwordboss.android.http.beans;

public class ShareHttpPost {
	private String receiver, data, sender_account, expiration_date, uuid, message, secure_item_type, status, nickname;
	private boolean visible;
	private int order;
	
	public ShareHttpPost() {
	}
	public ShareHttpPost(String receiver) {
		this.receiver = receiver;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getExpirationDate() {
		return expiration_date;
	}
	public void setExpirationDate(String expiration_date) {
		this.expiration_date = expiration_date;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getSenderAccount() {
		return sender_account;
	}
	public void setSenderAccount(String sender_account) {
		this.sender_account = sender_account;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getSecureItemType() {
		return secure_item_type;
	}
	public void setSecureItemType(String secure_item_type) {
		this.secure_item_type = secure_item_type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}