package com.mszychowiak.meishiscanner.tasks.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Pair;

public abstract class AbstractParsingTask implements ParseDataKeys{
	public static class Creator {
		public static AbstractParsingTask create(){
			AbstractParsingTask mainTask = new TelephoneParsingTask();
			mainTask.registerNextTask(new CompanyNameParsingTask());
			mainTask.registerNextTask(new PersonNameParsingTask());
			//TODO: add tasks;
			return mainTask;
		}
	}
	protected AbstractParsingTask nextTask;
	private final String key;
	
	protected AbstractParsingTask(String key) {
		this.key = key;
	}
	
	protected abstract List<String> parse(String data);
	
	protected final Map<String, List<String>> execute(String data, Map<String, List<String>> storage) {
		if (nextTask != null) {
			storage = nextTask.execute(data, storage);				
		}
		storage.put(this.key, parse(data));
		return storage;		
	}
	
	public final Map<String, List<String>> execute(String data) {				
		return execute(data, new HashMap<String, List<String>>());
	}
	
	protected final  void registerNextTask(AbstractParsingTask task) {
		if (nextTask != null) {
			nextTask.registerNextTask(task);
		} else {
			this.nextTask = task;
		}
	}

}
