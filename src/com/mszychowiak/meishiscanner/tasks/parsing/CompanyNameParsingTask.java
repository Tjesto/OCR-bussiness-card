package com.mszychowiak.meishiscanner.tasks.parsing;

import java.util.ArrayList;
import java.util.List;

public class CompanyNameParsingTask extends AbstractParsingTask {

	private static final int MAX_DIGITS_IN_CO_NAME = 4;

	protected CompanyNameParsingTask() {
		super(CO_NAME);
	}

	@Override
	protected List<String> parse(String data) {
		String[] subdata = data.split("\n|\n\r|\r|\r\n");
		List<String> cleanedSubdata = new ArrayList<String>();
		for (String s : subdata) {
			String subs = s.replaceAll("[^0-9\\+]", "");
			if (subs != null && subs.trim().length() <= MAX_DIGITS_IN_CO_NAME) {
				cleanedSubdata.add(s);
			}
		}
		List<String> companyNames = new ArrayList<String>();
		for (String s : cleanedSubdata) {
			if (s.toLowerCase().contains("sp. z o.o.") || s.toLowerCase().contains("sp z o o") || s.toLowerCase().contains("spzoo")) {
				companyNames.clear();
				companyNames.add(s);
				return companyNames;
			}
			if (s.toLowerCase().contains("s.a.") || s.toLowerCase().contains("s a") || s.toLowerCase().contains(" sa")) {
				companyNames.clear();
				companyNames.add(s);
				return companyNames;
			}
			if (s.toLowerCase().contains("s.c.") || s.toLowerCase().contains("s c") || s.toLowerCase().contains(" sc")) {
				companyNames.clear();
				companyNames.add(s);
				return companyNames;
			}
			if (s.toLowerCase().contains("s.j.") || s.toLowerCase().contains("s j") || s.toLowerCase().contains(" sj")) {
				companyNames.clear();
				companyNames.add(s);
				return companyNames;				
			}
			companyNames.add(s);
		}
		return companyNames;
	}

}
