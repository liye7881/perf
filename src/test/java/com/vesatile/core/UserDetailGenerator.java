package com.vesatile.core;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vesatile.core.entity.UserDetail;

@Service
public class UserDetailGenerator {
	@Autowired
	private SessionFactory sessionFactory;

	public Map<Long, UserDetail> init(long count) {
		Session session = sessionFactory.getCurrentSession();
		Map<Long, UserDetail> results = new HashMap<Long, UserDetail>();

		for (long i = 1; i <= count; i++) {
			UserDetail userDetail = new UserDetail();
			userDetail.setUserId(Long.valueOf(i));
			userDetail.setUsername(String.valueOf(i));
			userDetail.setPassword(String.valueOf(i));

			session.save(userDetail);
			results.put(i, userDetail);
		}

		return results;
	}
}
