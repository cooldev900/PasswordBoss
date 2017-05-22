package com.passwordboss.android.http.beans;

public class SharesHttpBean {

	private SentHttpBean received[];
	private SentHttpBean sent[];

	public SentHttpBean[] getSent() {
		return sent;
	}

	public void setSent(SentHttpBean[] sent) {
		this.sent = sent;
	}

	public SentHttpBean[] getReceived() {
		return received;
	}

	public void setReceived(SentHttpBean[] received) {
		this.received = received;
	}
}
