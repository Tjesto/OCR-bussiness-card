package com.mszychowiak.meishiscanner.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mszychowiak.meishiscanner.R;
import com.mszychowiak.meishiscanner.tasks.parsing.ParseDataKeys;
import com.mszychowiak.meishiscanner.ui.adapters.ContactsFieldsAdapter;
import com.mszychowiak.meishiscanner.ui.controller.ContactsController;
import com.mszychowiak.meishiscanner.ui.model.ContactsField;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class DataResultActivity extends Activity {
	
	private static final String KEYS = "extra.keys";
	private static final String RESULTS = "extra.bundle.results";
	
	private enum Fields {
		PHONE(ParseDataKeys.TELEPHONE, R.string.field_telephone),
		CO_NAME(ParseDataKeys.CO_NAME, R.string.field_co),
		EMAIL(ParseDataKeys.EMAIL, R.string.field_email),
		WEBSITE(ParseDataKeys.WEBSITE, R.string.field_web),
		NAME(ParseDataKeys.NAME, R.string.field_name),
		SURNAME(ParseDataKeys.SURNAME, R.string.field_surname);
		
		private final String key;
		final int text;
		
		private Fields(String key, int textId) {
			this.key = key;
			this.text = textId;
		}

		public static Fields getFromKey(String key) {
			for (Fields f : values()) {
				if (f.key.equals(key)) {
					return f;
				}
			}
			return null;
		}
		
		public static Fields getFromTextId(int textId) {
			for (Fields f : values()) {
				if (f.text == textId) {
					return f;
				}
			}
			return null;
		}
		
		
	}

	private ContactsController contactsController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_result);
		AbsListView fields = (AbsListView) findViewById(R.id.fileds);
		fields.setAdapter(createAdapter(getIntent()));
		this.contactsController = new ContactsController(this);
		findViewById(R.id.save_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveContact();											
			}
		});
		findViewById(R.id.discard_button).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
	}

	protected void saveContact() {
		AbsListView fields = (AbsListView) findViewById(R.id.fileds);
		Bundle contactBundle = new Bundle();		
		ArrayList<String> contactKeys = new ArrayList<String>();
		for (int i = 0; i < fields.getAdapter().getCount(); i++) {
			//Log.d("mszychowiak", ((ContactsField) fields.getAdapter().getItem(i)).getSelected());
			ContactsField field = (ContactsField) fields.getAdapter().getItem(i);
			String key = Fields.getFromTextId(field.getNameId()).key;
			contactKeys.add(key);
			contactBundle.putString(key, field.getSelected());
		}
		contactBundle.putStringArrayList(ContactsController.KEYS_LIST, contactKeys);
		if (contactsController != null) {
			try {
				contactsController.saveContacts(contactBundle);
				onSuccess();
			} catch (RemoteException e) {
				e.printStackTrace();
				onFailure();
			} catch (OperationApplicationException e) {
				e.printStackTrace();
				onFailure();
			}
		}
	}

	private void onSuccess() {
		new AlertDialog.Builder(DataResultActivity.this)
		.setMessage(R.string.saving_passed)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();						
			}
		}).show();		
	}

	private void onFailure() {
		new AlertDialog.Builder(DataResultActivity.this)
		.setMessage(R.string.saving_failed)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();						
			}
		}).show();		
	}

	private ListAdapter createAdapter(Intent intent) {
		if (intent == null || intent.getBundleExtra(RESULTS) == null) {
			finish();
			return new ArrayAdapter<String>(this, R.layout.text_view);
		}
		Bundle resultsBundle = intent.getBundleExtra(RESULTS); 
		List<String> keys = resultsBundle.getStringArrayList(KEYS);
		List<ContactsField> contactsFields = new ArrayList<ContactsField>();
		for (String key : keys) {
			List<String> values = resultsBundle.getStringArrayList(key);
			if (ParseDataKeys.P_NAME.equals(key)) {
				ContactsField nameField = new ContactsField(R.string.field_name, getNamesFrom(values));
				ContactsField surnameField = new ContactsField(R.string.field_surname, getSurnamesFrom(values));
				contactsFields.add(nameField);
				contactsFields.add(surnameField);
				continue;
			} 
			contactsFields.add(new ContactsField(Fields.getFromKey(key).text, values));
		}
		return new ContactsFieldsAdapter(this, R.layout.field_layout, contactsFields);
	}

	private List<String> getSurnamesFrom(List<String> values) {
		List<String> surnames = new ArrayList<String>();
		for (String val : values) {
			String[] fullName = val.split(" ");
			//TODO: improve registering names and surnames
			if (fullName.length > 1 && fullName.length < 3) {
				surnames.add(fullName[fullName.length - 1]);
			}
		}
		return surnames;
	}

	private List<String> getNamesFrom(List<String> values) {
		List<String> names = new ArrayList<String>();
		for (String val : values) {
			String[] fullName = val.split(" ");			
			//TODO: improve registering names and surnames
			if (fullName.length > 1 && fullName.length < 3) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < fullName.length - 1; i++) {
					if (i > 0) {
						sb.append(" ");
					}
					sb.append(fullName[i]);
				}
				names.add(sb.toString());
			}
		}
		return names;	
	}

	public static void startWith(Activity activity,
			Map<String, List<String>> parsingResults) {		
		activity.startActivity(createIntent(activity, parsingResults));
	}

	private static Intent createIntent(Activity activity, Map<String, List<String>> parsingResults) {
		Intent startIntent = new Intent(activity, DataResultActivity.class);
		ArrayList<String> keyList = new ArrayList<String>();
		keyList.addAll(parsingResults.keySet());
		Bundle resultsBundle = new Bundle();
		resultsBundle.putStringArrayList(KEYS, keyList);
		for (String key : keyList) {
			ArrayList<String> list = new ArrayList<String>();
			list.addAll(parsingResults.get(key));
			if (list.size() > 0) {
				resultsBundle.putStringArrayList(key, list);
			}
		}
		startIntent.putExtra(RESULTS, resultsBundle);
		return startIntent;
	}
}
