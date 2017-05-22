package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.Currency;

import java.sql.SQLException;



public class CurrencyBll extends BaseBll<Currency, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	public CurrencyBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getCurrencyDao());
		this.mDatabseHelper = mDatabseHelper;
	}
}