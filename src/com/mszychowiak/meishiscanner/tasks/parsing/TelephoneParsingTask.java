package com.mszychowiak.meishiscanner.tasks.parsing;

import java.util.ArrayList;
import java.util.List;

public class TelephoneParsingTask extends AbstractParsingTask {

	protected TelephoneParsingTask() {
		super(TELEPHONE);		
	}

	@Override
	protected List<String> parse(String data) {
		String[] subdata = data.split("\n|\n\r|\r|\r\n");
		List<String> cleanedSubdata = new ArrayList<String>();
		for (String s : subdata) {
			String subs = s.replaceAll("[^0-9\\+]", "");
			if (subs != null && subs.trim().length() > 0) {
				cleanedSubdata.add(subs);
			}
		}
		List<String> phoneNumbers = new ArrayList<String>();
		for (String s : cleanedSubdata) {
			if (s.contains("+") && !s.startsWith("+")) {
				continue;
			}
			if (s.contains("+") && s.length() != 12) {
				continue;
			}
			if (!s.contains("+") && s.length() != 9) {
				continue;
			}
			if (s.lastIndexOf("+") > 0) {
				continue;
			}
			phoneNumbers.add(s);
		}
		return phoneNumbers;
	}

}
