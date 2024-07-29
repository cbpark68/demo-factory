package com.demo.factory.common.service;

import com.demo.factory.common.domain.AccessLog;
import com.demo.factory.common.repository.AccessLogRepository;
import com.demo.factory.security.dto.MemberDtoForSecurity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccessLogServiceImpl implements AccessLogService {

	private final AccessLogRepository repository;

	@Override
	public void register(AccessLog accessLog) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		try {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			username = userDetails.getUsername();
		} catch (ClassCastException e) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof MemberDtoForSecurity) {
				username = ((MemberDtoForSecurity)authentication.getPrincipal()).getUsername();
			}
		} catch (Exception e) {
			username = (String) authentication.getPrincipal();
		}
		log.info("AccessLog username=" + username);
		accessLog.setUsername(username);
		repository.save(accessLog);
	}

	@Override
	public List<AccessLog> list() throws Exception {
		return repository.findAll(Sort.by(Direction.DESC, "logNo"));
	}

}
