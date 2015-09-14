package com.mszychowiak.meishiscanner.ui.model;

import java.util.ArrayList;
import java.util.List;

public class ContactsField {
	
	private final int nameId;
	private final List<String> options;
	private String selectedOption;
	
	public ContactsField(int nameId, List<String> options) {
		this.nameId = nameId;
		this.options = new ArrayList<String>();
		this.options.addAll(options);
		selectedOption = options.size() > 0 ? options.get(0) : null;
	}
	
	public int getNameId() {
		return nameId;
	}
	
	public List<String> getOptions(){
		List<String> result = new ArrayList<String>();
		result.addAll(this.options);
		return result;
	}

	public void setSelected(String selectedOption) {
		this.selectedOption = selectedOption;
	}
	
	public String getSelected() {
		return this.selectedOption;
	}
}
