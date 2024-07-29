package com.demo.factory.common.service;


import com.demo.factory.common.domain.LoginLog;

import java.util.List;

public interface LoginLogService {

	public void register(LoginLog loginLog) throws Exception;

	public List<LoginLog> list() throws Exception;

}
