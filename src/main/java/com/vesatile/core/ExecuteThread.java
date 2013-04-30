package com.vesatile.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.vesatile.core.service.ConsumeService;

public class ExecuteThread implements Callable<List<Object>> {
	private Object task;

	private ConsumeService[] consumeServices;

	public ExecuteThread(Object task, ConsumeService[] consumeServices) {
		this.task = task;
		this.consumeServices = consumeServices;
	}

	@Override
	public List<Object> call() {
		List<Object> objects = new ArrayList<Object>();

		for (ConsumeService consumeService : consumeServices) {
			Object result = consumeService.execute(task);
			if (result != null) {
				objects.add(result);
			}
		}

		return objects;
	}
}