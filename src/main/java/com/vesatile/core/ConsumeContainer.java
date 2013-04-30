package com.vesatile.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.vesatile.core.service.ConsumeService;

public class ConsumeContainer extends Thread {
	private static class ExecuteThread implements Callable<List<Object>> {
		private Object task;

		private ConsumeService[] consumeServices;

		public ExecuteThread(Object task, ConsumeService[] consumeServices) {
			super();
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

	private static Logger logger = Logger.getLogger(ConsumeContainer.class);

	private ExecutorService executor = Executors.newFixedThreadPool(5);

	private Queue<Object> tasks = new LinkedList<Object>();

	private ConcurrentMap<Object, Future<List<Object>>> results = new ConcurrentHashMap<Object, Future<List<Object>>>();

	@Autowired
	private ConsumeService[] consumeServices;

	@Autowired
	private ExecutingResult executingResult;

	public void addTask(Object task) {
		synchronized (tasks) {
			tasks.add(task);
		}
	}

	public List<Object> getResult(Object task) {
		synchronized (task) {
			Future<List<Object>> future = results.get(task);
			while (future == null && tasks.contains(task)) {
				future = results.get(task);
			}

			List<Object> result = null;
			try {
				if (future != null)
					result = future.get();
			} catch (InterruptedException e) {
				logger.error("", e);
			} catch (ExecutionException e) {
				logger.error("", e);
			}

			return result;
		}
	}

	@Override
	public void run() {
		while (true) {
			synchronized (tasks) {
				if (!tasks.isEmpty()) {
					Object task = tasks.poll();
					synchronized (task) {
						results.put(task, executor.submit(new ExecuteThread(
								task, consumeServices)));
					}
				}
			}
		}
	}
}
