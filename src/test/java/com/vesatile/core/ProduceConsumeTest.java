package com.vesatile.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vesatile.core.utils.FileUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ProduceConsumeTest {
	private static Logger logger = Logger.getLogger(ProduceConsumeTest.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserDetailGenerator userDetailGenerator;

	@Autowired
	private ConsumeContainer consumeContainer;

	@Test
	@Transactional
	public void testSave() throws IOException {
		long start = System.currentTimeMillis();

		List<String> urls = FileUtils.read(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream("url.txt"));
		List<StringBuffer> tasks = new ArrayList<StringBuffer>();
		for (String url : urls) {
			StringBuffer task = new StringBuffer(url);
			consumeContainer.addTask(task);
			tasks.add(task);
		}
		for (StringBuffer task : tasks) {
			consumeContainer.getResult(task);
		}

		logger.warn("Parallel execute in "
				+ (System.currentTimeMillis() - start));
	}
}
