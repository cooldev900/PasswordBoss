package com.passwordboss.android.analytics;

public class DailyAnalyticsHelper {
	long pv_cnt, dw_cnt, pi_cnt, total_logins, total_pwd_cnt, weak_pwds_cnt,
			duplicate_pwds_cnt, old_pwds_cnt;
	double dup_pwd_score, old_pwd_score, weak_pwd_score, total_pwd_score;

	public DailyAnalyticsHelper() {
 
		setDup_pwd_score(40);
		setOld_pwd_score(20);
		setWeak_pwd_score(40);
		setTotal_pwd_score(0);
	}

	public long getPv_cnt() {
		return pv_cnt;
	}

	public void setPv_cnt(long pv_cnt) {
		this.pv_cnt = pv_cnt;
	}

	public long getDw_cnt() {
		return dw_cnt;
	}

	public void setDw_cnt(long dw_cnt) {
		this.dw_cnt = dw_cnt;
	}

	public long getPi_cnt() {
		return pi_cnt;
	}

	public void setPi_cnt(long pi_cnt) {
		this.pi_cnt = pi_cnt;
	}

	public long getTotal_logins() {
		return total_logins;
	}

	public void setTotal_logins(long total_logins) {
		this.total_logins = total_logins;
	}

	public long getTotal_pwd_cnt() {
		return total_pwd_cnt;
	}

	public void setTotal_pwd_cnt(long total_pwd_cnt) {
		this.total_pwd_cnt = total_pwd_cnt;
	}

	public long getWeak_pwds_cnt() {
		return weak_pwds_cnt;
	}

	public void setWeak_pwds_cnt(long weak_pwds_cnt) {
		this.weak_pwds_cnt = weak_pwds_cnt;
	}

	public long getDuplicate_pwds_cnt() {
		return duplicate_pwds_cnt;
	}

	public void setDuplicate_pwds_cnt(long duplicate_pwds_cnt) {
		this.duplicate_pwds_cnt = duplicate_pwds_cnt;
	}

	public long getOld_pwds_cnt() {
		return old_pwds_cnt;
	}

	public void setOld_pwds_cnt(long old_pwds_cnt) {
		this.old_pwds_cnt = old_pwds_cnt;
	}

	public double getDup_pwd_score() {
		return dup_pwd_score;
	}

	public void setDup_pwd_score(double dup_pwd_score) {
		this.dup_pwd_score = dup_pwd_score;
	}

	public double getOld_pwd_score() {
		return old_pwd_score;
	}

	public void setOld_pwd_score(double old_pwd_score) {
		this.old_pwd_score = old_pwd_score;
	}

	public double getWeak_pwd_score() {
		return weak_pwd_score;
	}

	public void setWeak_pwd_score(double weak_pwd_score) {
		this.weak_pwd_score = weak_pwd_score;
	}

	public double getTotal_pwd_score() {
		return total_pwd_score;
	}

	public void setTotal_pwd_score(double total_pwd_score) {
		this.total_pwd_score = total_pwd_score;
	}

}
