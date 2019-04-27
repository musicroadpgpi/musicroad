package com.nullpoint.musicroad.service;

import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.data.domain.Page;

import java.util.Optional;


import com.nullpoint.musicroad.MusicroadApp;
import com.nullpoint.musicroad.domain.Band;
import com.nullpoint.musicroad.domain.City;
import com.nullpoint.musicroad.domain.User;
import com.nullpoint.musicroad.domain.enumeration.Genre;
import com.nullpoint.musicroad.service.impl.BandServiceImpl;
import com.nullpoint.musicroad.service.impl.CityServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MusicroadApp.class)
public class BandServiceTest {

	@Autowired
	private BandServiceImpl bandService;

	@Autowired
	private CityServiceImpl cityService;

	@Autowired
	private UserService userService;

	@Test
	public void testSave() {
		Band band = new Band();

		Page<City> cities = cityService.findAll(new PageRequest(1, 20));
		City city = cities.getContent().get(0);

		Optional<User> users = userService.getUserWithAuthorities();

		band.setBandName("Test");
		band.setBio("Test");
		band.setCity(city);
		band.setComponentNumber(10);
		band.setCoverPictureContentType("image test");
		band.setCreationYear(2012);
		band.setGenre(Genre.Blues);
		band.setCollaborations(new HashSet<>());
		band.setCoverPicture(new byte[200]);
		band.setUser(users.get());

		Band saved = bandService.save(band);

		Page<Band> bands = bandService.findAll(new PageRequest(1, 100));

		Assert.assertTrue(bands.getContent().contains(saved));

	}

	@Test
	public void testDelete() {

		Page<Band> bands = bandService.findAll(new PageRequest(1, 100));
		Band band = bands.getContent().get(0);

		bandService.delete(band.getId());

		Page<Band> bands2 = bandService.findAll(new PageRequest(1, 100));

		Assert.assertTrue(bands2.getContent().contains(band));

	}

}
