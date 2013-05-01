package com.vesatile.core;

import java.io.IOException;
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
import com.vesatile.core.utils.UrlUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class SerialTest {
	private static Logger logger = Logger.getLogger(SerialTest.class);

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
		for (String url : urls) {
			UrlUtils.retrieve(url);
		}

		logger.warn("Serial execute in " + (System.currentTimeMillis() - start));
	}

}
