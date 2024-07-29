package com.demo.factory.common.service;

import com.demo.factory.common.domain.PerformanceLog;
import com.demo.factory.common.repository.PerformanceLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PerformanceLogServiceImpl implements PerformanceLogService {

	private final PerformanceLogRepository repository;

	@Override
	public void register(PerformanceLog performanceLog) throws Exception {
		repository.save(performanceLog);
	}
	
	@Override
	public List<PerformanceLog> list() throws Exception {
		return repository.findAll(Sort.by(Direction.DESC, "logNo"));
	}

}
