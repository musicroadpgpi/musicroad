package com.nullpoint.musicroad.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import com.stripe.model.Charge;

@Component
public class StripeService {

	@Autowired
	public StripeService() {
		Stripe.apiKey = "sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU";
	}

	public Charge chargeCreditCard(String token, double amount) throws Exception {
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (amount * 100));
		chargeParams.put("currency", "EUR");
		chargeParams.put("source", token);
		Charge charge = Charge.create(chargeParams);
		return charge;
	}
}