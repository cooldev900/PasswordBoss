package com.passwordboss.android.database.bll;

import com.passwordboss.android.database.DatabaseHelperNonSecure;
import com.passwordboss.android.database.beans.MessageTranslations;

import java.sql.SQLException;


public class MessageTranslationsBll extends BaseBll<MessageTranslations, Integer> {

	private DatabaseHelperNonSecure mDatabseHelper;
	
	public MessageTranslationsBll(DatabaseHelperNonSecure mDatabseHelper) throws SQLException {
		super(mDatabseHelper.getMessageTranslationsDao());
		this.mDatabseHelper = mDatabseHelper;
	}
	
	public void updateObject(MessageTranslations messageTranslations) {
		try {
			
			String updateQuery = "UPDATE message_translations SET title=?, " +
					"title1=?, title2=?, body=?, body1=?, body2=?, confirm_action_text=?," +
					"cancel_action_text=?, created_date=?, last_modified_date=? WHERE msg_id=? AND lang=?";
			mDao.executeRaw(updateQuery, 
					messageTranslations.getTitle(),
					messageTranslations.getTitle1(),
					messageTranslations.getTitle2(),
					messageTranslations.getBody(),
					messageTranslations.getBody1(),
					messageTranslations.getBody2(),
					messageTranslations.getConfirmActionText(),
					messageTranslations.getCancelActionText(),
					messageTranslations.getCreatedDate(),
					messageTranslations.getLastModifiedDate(),
					messageTranslations.getMsgId().getMsgId(),
					messageTranslations.getLang());
		} catch (Exception e) {
			////Log.print(e);
		}
	}
}
