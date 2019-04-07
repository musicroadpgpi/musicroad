import { Component, OnInit } from '@angular/core';
import { Module as StripeModule, StripeScriptTag, StripeSource, StripeToken } from 'stripe-angular';

@Component({
    selector: 'jhi-payment',
    templateUrl: './payment.component.html',
    styles: []
})
export class PaymentComponent {
    private publishableKey: string = 'sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU';

    constructor(public StripeScriptTag: StripeScriptTag) {
        this.StripeScriptTag.setPublishableKey(this.publishableKey);
    }

    extraData = {
        name: null,
        address_city: null,
        address_line1: null,
        address_line2: null,
        address_state: null,
        address_zip: null
    };

    onStripeInvalid(error: Error) {
        console.log('Validation Error', error);
    }

    setStripeToken(token: StripeToken) {
        console.log('Stripe token', token);
    }

    setStripeSource(source: StripeSource) {
        console.log('Stripe source', source);
    }

    /*onStripeError( error:Error ){
        console.error('Stripe error', token)
    }*/
}
