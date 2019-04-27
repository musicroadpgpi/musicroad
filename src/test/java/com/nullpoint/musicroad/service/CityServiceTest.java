package com.nullpoint.musicroad.service;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nullpoint.musicroad.MusicroadApp;
import com.nullpoint.musicroad.domain.City;
import com.nullpoint.musicroad.service.impl.CityServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MusicroadApp.class)
public class CityServiceTest {

	@Autowired
	private CityServiceImpl cityService;

	@Test
	public void testSave() {
		City city = new City();

		city.setCountry("Country Test");
		city.setName("Name Test");

		City saved = cityService.save(city);

		Page<City> cities = cityService.findAll(new PageRequest(1, 20));

		Assert.assertTrue(cities.getContent().contains(saved));

	}

	@Test
	public void testDelete() {

		Page<City> cities = cityService.findAll(new PageRequest(1, 20));
		City city = cities.getContent().get(0);

		cityService.delete(city.getId());

		Page<City> cities2 = cityService.findAll(new PageRequest(1, 20));

		Assert.assertTrue(cities2.getContent().contains(city));

	}

}
