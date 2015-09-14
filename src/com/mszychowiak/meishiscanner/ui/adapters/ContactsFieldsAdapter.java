package com.mszychowiak.meishiscanner.ui.adapters;

import java.util.List;

import com.mszychowiak.meishiscanner.R;
import com.mszychowiak.meishiscanner.ui.model.ContactsField;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ContactsFieldsAdapter extends ArrayAdapter<ContactsField> {

	public ContactsFieldsAdapter(Context context, int resource, List<ContactsField> objects) {
		super(context, resource, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View baseView = View.inflate(getContext(), R.layout.field_layout, null);
		final ContactsField field = getItem(position);
		TextView name = (TextView) baseView.findViewById(R.id.header);
		name.setText(field.getNameId());
		Spinner optionsSpinner = (Spinner) baseView.findViewById(R.id.options_spinner);
		optionsSpinner.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.text_view, field.getOptions()));
		optionsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				field.setSelected(field.getOptions().get(pos));
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing;
				
			}
		});
		return baseView;
	}

}
