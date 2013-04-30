package com.vesatile.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vesatile.core.entity.UserDetail;

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
	public void testSave() {
		userDetailGenerator.init(10000);

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDetail.class);
		criteria.addOrder(Order.desc("id"));

		long start = System.currentTimeMillis();

		List<UserDetail> details = new ArrayList<UserDetail>();
		ScrollableResults scroll = criteria.scroll();
		while (scroll.next()) {
			UserDetail userDetail = (UserDetail) scroll.get(0);
			consumeContainer.addTask(userDetail);
			details.add(userDetail);
		}

		for (UserDetail userDetail : details) {
			List<Object> result = consumeContainer.getResult(userDetail);
			Assert.assertEquals(1, result.size());
			Assert.assertEquals(userDetail, result.get(0));
		}

		logger.warn("Parallel execute in "
				+ (System.currentTimeMillis() - start));
	}
}
