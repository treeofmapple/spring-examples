package com.pogeku.service.auth.auditing;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class ApplicationAuditAware implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		return Optional.empty();
	}

}
