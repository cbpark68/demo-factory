package com.demo.factory.common.service;


import com.demo.factory.common.domain.PerformanceLog;

import java.util.List;

public interface PerformanceLogService {

	public void register(PerformanceLog performanceLog) throws Exception;
	
	public List<PerformanceLog> list() throws Exception;

}
