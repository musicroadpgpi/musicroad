package com.nullpoint.musicroad.web.rest;

import com.nullpoint.musicroad.service.StripeService;
import com.stripe.model.Charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/payment")
public class PaymentResource {

	private StripeService stripeClient;

	@Autowired
	public PaymentResource(StripeService stripeClient) {
		this.stripeClient = stripeClient;
	}

	@PostMapping("/charge")
	public Charge chargeCard(HttpServletRequest request) throws Exception {
		String token = request.getHeader("token");
		Double amount = Double.parseDouble(request.getHeader("amount"));
		return this.stripeClient.chargeCreditCard(token, amount);
	}
}