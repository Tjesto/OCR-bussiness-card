package com.mszychowiak.meishiscanner.tasks.parsing;

import java.util.ArrayList;
import java.util.List;

public class PersonNameParsingTask extends AbstractParsingTask {

	protected PersonNameParsingTask() {
		super(P_NAME);
	}

	@Override
	protected List<String> parse(String data) {
		String[] subdata = data.split("\n|\n\r|\r|\r\n");
		List<String> cleanedSubdata = new ArrayList<String>();
		for (String s : subdata) {
			String subs = s.replaceAll("[^\\w\\s]","").replaceAll("[^0-9]", "");
			if (subs != null && subs.trim().length() ==0) {
				cleanedSubdata.add(s);
			}
		}
		List<String> personNames = new ArrayList<String>();
		for (String s : cleanedSubdata) {
			if (s.toLowerCase().contains("sp. z o.o.") || s.toLowerCase().contains("sp z o o") || s.toLowerCase().contains("spzoo")) {
				continue;
			}
			if (s.toLowerCase().contains("s.a.") || s.toLowerCase().contains("s a") || s.toLowerCase().contains(" sa")) {
				continue;
			}
			if (s.toLowerCase().contains("s.c.") || s.toLowerCase().contains("s c") || s.toLowerCase().contains(" sc")) {
				continue;
			}
			if (s.toLowerCase().contains("s.j.") || s.toLowerCase().contains("s j") || s.toLowerCase().contains(" sj")) {
				continue;
			}
			if (!s.toLowerCase().contains(" ")) {
				continue;
			}
			personNames.add(s);
		}
		return personNames;
	}

}
