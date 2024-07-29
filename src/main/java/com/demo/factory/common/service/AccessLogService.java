package com.demo.factory.common.service;


import com.demo.factory.common.domain.AccessLog;

import java.util.List;

public interface AccessLogService {

	public void register(AccessLog accessLog) throws Exception;
	
	public List<AccessLog> list() throws Exception;

}
