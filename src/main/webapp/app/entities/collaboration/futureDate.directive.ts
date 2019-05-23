import { Directive, Injectable, Input } from '@angular/core';
import { NG_ASYNC_VALIDATORS, AsyncValidator, AsyncValidatorFn, FormControl, ValidationErrors } from '@angular/forms';
import { Observable, of } from 'rxjs';
import * as moment from 'moment';

@Directive({
    selector: '[futureDate][ngModel]',
    providers: [
        {
            provide: NG_ASYNC_VALIDATORS,
            useExisting: FutureDateValidator,
            multi: true
        }
    ]
})
// @Injectable({providedIn: 'root'})
export class FutureDateValidator implements AsyncValidator {
    validator: AsyncValidatorFn;

    constructor() {
        this.validator = this.futureDateValidator();
    }

    validate(c: FormControl) {
        return this.validator(c);
    }

    futureDateValidator(): AsyncValidatorFn {
        return (c: FormControl) => {
            return this.generateObservable(c.value);
        };
    }

    generateObservable(expYear: moment.Moment): Observable<ValidationErrors | null> {
        return of(this.validation(expYear));
    }

    validation(expYear: moment.Moment): ValidationErrors | null {
        if (this.evaluateCreationYear(expYear)) {
            return null;
        } else {
            return {
                futureDate: {
                    valid: false
                }
            };
        }
    }

    evaluateCreationYear(proposedDate: moment.Moment): boolean {
        const dateNow: Date = new Date(Date.now());
        const pDate: Date = proposedDate.toDate();
        if (pDate > dateNow) {
            return true;
        } else {
            return false;
        }
    }
}
