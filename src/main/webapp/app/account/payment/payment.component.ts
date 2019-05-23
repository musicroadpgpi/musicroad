import { Component, OnInit } from '@angular/core';
import { Module as StripeModule, StripeScriptTag, StripeSource, StripeToken } from 'stripe-angular';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { SERVER_API_URL } from 'app/app.constants';
import { JhiAlertService, JhiAlert } from 'ng-jhipster';
import { now } from 'moment';

@Component({
    selector: 'jhi-payment',
    templateUrl: './payment.component.html',
    styles: ['payment.css']
})
export class PaymentComponent {
    private publishableKey = 'sk_test_eXsDnOULTLH9SkICbIpfbIw200QEiGs4GU';

    numberCardBlank = false;
    numberCardNotNumber = false;
    numberCardNotValid = false;

    expirationMonthBlank = false;
    expirationYearBlank = false;
    expirationMonthNotNumber = false;
    expirationYearNotNumber = false;
    expirationDateIsPast = false;

    cvcBlank = false;
    cvcNotNumber = false;
    cvcNotInRange = false;

    validCreditCardNumbers = [
        '4242424242424242',
        '4000056655665556',
        '5555555555554444',
        '2223003122003222',
        '5200828282828210',
        '5105105105105100',
        '378282246310005',
        '371449635398431',
        '6011111111111117',
        '6011000990139424',
        '30569309025904',
        '38520000023237',
        '3566002020360505',
        '6200000000000005'
    ];

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
        this.resetErrorMessages();
        const form = document.getElementsByTagName('form')[0];
        form.cardNumber.value = form.cardNumber.value.trim();
        form.expMonth.value = form.expMonth.value.trim();
        form.expYear.value = form.expYear.value.trim();
        const numberCard: string = form.cardNumber.value;
        const expMonth: string = form.expMonth.value;
        const expYear: string = form.expYear.value;
        const cvc: string = form.cvc.value;
        this.evaluateCreditCardNumber(numberCard);
        this.evaluateExpirationDate(expMonth, expYear);
        this.evaluateCvc(cvc);
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
                    console.log(response);
                    this.jhiAlertService.error('global.menu.account.paymentError', null, null);
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

    resetErrorMessages() {
        this.numberCardBlank = false;
        this.numberCardNotNumber = false;
        this.numberCardNotValid = false;
        this.expirationMonthBlank = false;
        this.expirationYearBlank = false;
        this.expirationMonthNotNumber = false;
        this.expirationYearNotNumber = false;
        this.expirationDateIsPast = false;
        this.cvcNotNumber = false;
        this.cvcNotInRange = false;
    }

    evaluateCreditCardNumber(numberCard: string) {
        if (numberCard === '') {
            this.numberCardBlank = true;
        }
        if (isNaN(Number(numberCard)) || numberCard.includes(',') || numberCard.includes('.')) {
            this.numberCardNotNumber = true;
        }
        if (!this.validCreditCardNumbers.includes(numberCard)) {
            this.numberCardNotValid = true;
        }
    }

    evaluateExpirationDate(expMonth: string, expYear: string) {
        if (expMonth === '') {
            this.expirationMonthBlank = true;
        }
        if (isNaN(Number(expMonth)) || expMonth.includes(',') || expMonth.includes('.')) {
            this.expirationMonthNotNumber = true;
        } else if (Number(expMonth).valueOf() > 12 || Number(expMonth).valueOf() < 1) {
            this.expirationMonthNotNumber = true;
        }
        if (expYear === '') {
            this.expirationYearBlank = true;
        }
        if (isNaN(Number(expYear)) || expYear.includes(',') || expYear.includes('.')) {
            this.expirationYearNotNumber = true;
        }
        if (!(this.expirationMonthBlank || this.expirationYearBlank || this.expirationMonthNotNumber || this.expirationYearNotNumber)) {
            const now: Date = new Date(Date.now());
            const year: Number = new Number(expYear);
            const month: Number = new Number(expMonth);
            if (now.getFullYear() > year) {
                this.expirationDateIsPast = true;
            } else if (now.getFullYear() === year.valueOf() && now.getMonth() > month.valueOf()) {
                this.expirationDateIsPast = true;
            }
        }
    }

    evaluateCvc(cvc: string) {
        if (cvc !== '') {
            if (isNaN(Number(cvc)) || cvc.includes(',') || cvc.includes('.')) {
                this.cvcNotNumber = true;
            }
            if (!isNaN(Number(cvc))) {
                const cvcNumber: number = new Number(cvc).valueOf();
                if (cvcNumber < 100 || cvcNumber > 999) {
                    this.cvcNotInRange = true;
                }
            }
        }
    }
}
