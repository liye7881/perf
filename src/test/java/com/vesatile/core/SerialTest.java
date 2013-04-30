package com.vesatile.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
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
public class SerialTest {
	private static Logger logger = Logger.getLogger(SerialTest.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserDetailGenerator userDetailGenerator;

	@Autowired
	private ConsumeContainer consumeContainer;

	@SuppressWarnings("unchecked")
	@Test
	@Transactional
	public void testSave() {
		userDetailGenerator.init(100);

		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(UserDetail.class);
		criteria.addOrder(Order.asc("id"));

		List<UserDetail> details = (List<UserDetail>) criteria.list();
		for (UserDetail userDetail : details) {
			if (logger.isInfoEnabled()) {
				logger.info(userDetail);
			}
		}
	}

}
