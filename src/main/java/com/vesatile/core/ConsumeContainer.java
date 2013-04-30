package com.vesatile.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vesatile.core.service.ConsumeService;

@Component
public class ConsumeContainer {
	private static Logger logger = Logger.getLogger(ConsumeContainer.class);

	private ExecutorService executor;

	private ConcurrentMap<Object, Future<List<Object>>> results = new ConcurrentHashMap<Object, Future<List<Object>>>();

	public ConsumeContainer() {
		executor = new ThreadPoolExecutor(0, 20, 5 * 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

	@Autowired
	private ConsumeService[] consumeServices;

	public void addTask(Object task) {
		results.put(task,
				executor.submit(new ExecuteThread(task, consumeServices)));
	}

	public List<Object> getResult(Object task) {
		Future<List<Object>> future = results.get(task);
		List<Object> result = new ArrayList<Object>();
		try {
			if (future != null) {
				result.addAll(future.get());
			}
		} catch (InterruptedException e) {
			logger.error("", e);
		} catch (ExecutionException e) {
			logger.error("", e);
		} finally {
			results.remove(task);
		}

		return result;
	}
}
