package com.nullpoint.musicroad.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nullpoint.musicroad.MusicroadApp;
import com.stripe.model.Charge;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MusicroadApp.class)
public class StripeServiceTest {

	@Autowired
	private StripeService stripeService;

	@Test
	public void testChangeCreditCard() throws Exception {
		Charge charge = stripeService.chargeCreditCard("sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU", 1000000);

		System.out.println(charge.getAmount());
	}

}
