import { Component, OnInit } from '@angular/core';
import { Module as StripeModule, StripeScriptTag, StripeSource, StripeToken } from 'stripe-angular';
import { HttpHeaders, HttpClient } from '@angular/common/http';

@Component({
    selector: 'jhi-payment',
    templateUrl: './payment.component.html',
    styles: ['payment.css']
})
export class PaymentComponent {
    private publishableKey: string = 'sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU';
    private http: HttpClient;

    constructor(public StripeScriptTag: StripeScriptTag) {
        this.StripeScriptTag.setPublishableKey(this.publishableKey);
    }

    /*   extraData = {
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

    /!*onStripeError( error:Error ){
        console.error('Stripe error', token)
    }*!/
*/

    chargeCreditCard() {
        let form = document.getElementsByTagName('form')[0];
        (<any>window).Stripe.card.createToken(
            {
                number: form.cardNumber.value,
                exp_month: form.expMonth.value,
                exp_year: form.expYear.value,
                cvc: form.cvc.value
            },
            (status: number, response: any) => {
                if (status === 200) {
                    let token = response.id;
                    this.chargeCard(token);
                } else {
                    console.log(response.error.message);
                }
            }
        );
    }

    chargeCard(token: string) {
        const headers = new HttpHeaders({ token: token, amount: '100' });
        this.http.post('http://localhost:9000/payment/charge', {}, { headers: headers }).subscribe(resp => {
            console.log(resp);
        });
    }
}
