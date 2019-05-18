package com.nullpoint.musicroad.service;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nullpoint.musicroad.MusicroadApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MusicroadApp.class)
public class AuditEventServiceTest {

	@Autowired
	private AuditEventService auditEventService;

	@Test
	public void testFindAll() {
		Page<AuditEvent> page = auditEventService.findAll(new PageRequest(1, 100));

		if (page.hasContent())
			for (AuditEvent auditEvent : page) {
				System.out.println("Audit Event: " + auditEvent.getPrincipal());
			}
	}

}
