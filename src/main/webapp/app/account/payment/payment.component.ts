import { Component, OnInit } from '@angular/core';
import { Module as StripeModule, StripeScriptTag, StripeSource, StripeToken } from 'stripe-angular';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { SERVER_API_URL } from 'app/app.constants';
import { JhiAlertService, JhiAlert } from 'ng-jhipster';

@Component({
    selector: 'jhi-payment',
    templateUrl: './payment.component.html',
    styles: ['payment.css']
})
export class PaymentComponent {
    private publishableKey = 'sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU';

    constructor(
        public stripeScriptTag: StripeScriptTag,
        protected jhiAlertService: JhiAlertService,
        protected http: HttpClient,
        protected authServerProvider: AuthServerProvider
    ) {
        this.stripeScriptTag.setPublishableKey(this.publishableKey);
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
        const form = document.getElementsByTagName('form')[0];
        (<any>window).Stripe.card.createToken(
            {
                number: form.cardNumber.value,
                exp_month: form.expMonth.value,
                exp_year: form.expYear.value,
                cvc: form.cvc.value
            },
            (status: number, response: any) => {
                if (status === 200) {
                    const token = response.id;
                    this.chargeCard(token);
                } else {
                    this.jhiAlertService.error('global.menu.account.paymentError', null, null);
                    this.previousState();
                    console.log(response.error.message);
                }
            }
        );
    }

    chargeCard(token: string) {
        const authToken = this.authServerProvider.getToken();
        const headers = new HttpHeaders({
            token: token,
            amount: '9.95'
            // Content-Type: 'application/json',
            // Accept: 'application/problem+json',
            // Authorization: 'Bearer: ' + authToken
        });
        this.http.post(SERVER_API_URL + '/api/payment/charge', {}, { headers: headers }).subscribe(resp => {
            if (resp !== undefined) {
                if (resp !== null) {
                    this.jhiAlertService.success('global.menu.account.paymentSuccess', null, null);
                    this.previousState();
                }
            }
        });
    }

    previousState() {
        window.history.back();
    }
}
