package com.vesatile.core.service;

import org.springframework.stereotype.Service;

import com.vesatile.core.utils.UrlUtils;

@Service
public class RemoteConsumeServiceImpl implements ConsumeService {

	@Override
	public Object execute(Object object) {
		StringBuffer task = (StringBuffer) object;
		return UrlUtils.retrieve(task.toString());
	}

}
