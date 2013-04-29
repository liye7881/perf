package com.vesatile.core.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogConsumeServiceImpl implements ConsumeService {
	private static Logger logger = Logger
			.getLogger(LogConsumeServiceImpl.class);

	@Override
	public Object execute(Object object) {
		if (logger.isDebugEnabled()) {
			logger.debug(object);
		}

		return object;
	}

}
