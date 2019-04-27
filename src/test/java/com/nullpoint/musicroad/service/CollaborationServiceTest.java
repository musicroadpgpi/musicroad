package com.nullpoint.musicroad.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.nullpoint.musicroad.MusicroadApp;
import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.domain.Collaboration;
import com.nullpoint.musicroad.domain.User;
import com.nullpoint.musicroad.service.impl.BandServiceImpl;
import com.nullpoint.musicroad.service.impl.CollaborationServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusicroadApp.class)
public class CollaborationServiceTest {

	@Autowired
	private CollaborationServiceImpl collaborationService;

	@Autowired
	private BandServiceImpl bandService;

	@Autowired
	private AuditingHandler auditingHandler;

	@Mock
	DateTimeProvider dateTimeProvider;

	private User user;

	@Before
	public void init() {
		user = new User();
		user.setLogin("johndoe");
		user.setPassword(RandomStringUtils.random(60));
		user.setActivated(true);
		user.setEmail("johndoe@localhost");
		user.setFirstName("john");
		user.setLastName("doe");
		user.setImageUrl("http://placehold.it/50x50");
		user.setLangKey("en");

		when(dateTimeProvider.getNow()).thenReturn(Optional.of(LocalDateTime.now()));
		auditingHandler.setDateTimeProvider(dateTimeProvider);
	}

	@Test
	public void testSave() {
		System.out.println("========== testSave() ==========");
		try {
			Pageable pageable = new PageRequest(1, 20);
			Page<Band> bands = this.bandService.findAll(pageable);

			Band band1 = bands.getContent().get(0);
			Band band2 = bands.getContent().get(1);

			Set<Band> banditas = new TreeSet<Band>();
			banditas.add(band1);
			banditas.add(band2);

			LocalDate proposedDate = LocalDate.of(2020, 04, 27);

			final Collaboration collaboration = new Collaboration();

			collaboration.setAccepted(true);
			collaboration.setBands(banditas);
			collaboration.setMessage("Hola");
			collaboration.setProposedDate(proposedDate);
			Assert.assertNotNull(collaboration);

			final Collaboration saved = this.collaborationService.save(collaboration);
			final Collection<Collaboration> collaborations = (Collection<Collaboration>) this.collaborationService
					.findAll(pageable);
			Assert.assertTrue(collaborations.contains(saved));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo, " + e.getMessage() + "!");
		}
	}

	@Test
	public void testDelete() {
		System.out.println("========== testDelete() ==========");

		try {

			Pageable pageable = new PageRequest(1, 20);
			Page<Collaboration> collabos = this.collaborationService.findAll(pageable);

			Collaboration collaboration = collabos.getContent().get(0);
			this.collaborationService.delete(collaboration.getId());

			final Collection<Collaboration> collaborations = (Collection<Collaboration>) this.collaborationService
					.findAll(pageable);
			Assert.assertTrue(!collaborations.contains(collaboration));

			System.out.println("¡Exito!");

		} catch (final Exception e) {
			System.out.println("¡Fallo," + e.getMessage() + "!");
		}

	}

}
