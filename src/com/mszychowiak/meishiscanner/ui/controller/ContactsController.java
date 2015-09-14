package com.mszychowiak.meishiscanner.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.mszychowiak.meishiscanner.tasks.parsing.ParseDataKeys;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;

public class ContactsController {

	public static final String KEYS_LIST = "keys.list";
	private Activity activity;

	public ContactsController(Activity activity) {
		this.activity = activity;
	}
	
	public void saveContacts(Bundle contactBundle) throws RemoteException, OperationApplicationException {
		ArrayList<ContentProviderOperation> ops =
		          new ArrayList<ContentProviderOperation>();
		List<String> keys = contactBundle.getStringArrayList(KEYS_LIST);
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());
		if (keys.contains(ParseDataKeys.TELEPHONE)) {
			String phone = contactBundle.getString(ParseDataKeys.TELEPHONE);
			String phoneNumberKey = phone.startsWith("+") ? Phone.NORMALIZED_NUMBER : Phone.NUMBER;
			ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValue(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(phoneNumberKey, phone)
				.withValue(Phone.TYPE, Phone.TYPE_MOBILE).build());
		}
		if (keys.contains(ParseDataKeys.NAME) || keys.contains(ParseDataKeys.SURNAME)) {
			String name = keys.contains(ParseDataKeys.NAME) ? contactBundle.getString(ParseDataKeys.NAME) : "";
			String surname = keys.contains(ParseDataKeys.SURNAME) ? contactBundle.getString(ParseDataKeys.SURNAME) : "";
			ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValue(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name)
				.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, surname).build());
		}
		activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
	}

}
