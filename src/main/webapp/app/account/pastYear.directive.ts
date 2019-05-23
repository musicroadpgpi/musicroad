import { Directive, Injectable, Input } from '@angular/core';
import { NG_ASYNC_VALIDATORS, AsyncValidator, AsyncValidatorFn, FormControl, ValidationErrors } from '@angular/forms';
import { Observable, of } from 'rxjs';

@Directive({
    selector: '[pastYear][ngModel]',
    providers: [
        {
            provide: NG_ASYNC_VALIDATORS,
            useExisting: PastYearValidator,
            multi: true
        }
    ]
})
// @Injectable({providedIn: 'root'})
export class PastYearValidator implements AsyncValidator {
    validator: AsyncValidatorFn;

    constructor() {
        this.validator = this.pastYearValidator();
    }

    validate(c: FormControl) {
        return this.validator(c);
    }

    pastYearValidator(): AsyncValidatorFn {
        return (c: FormControl) => {
            return this.generateObservable(c.value);
        };
    }

    generateObservable(expYear: string): Observable<ValidationErrors | null> {
        return of(this.validation(expYear));
    }

    validation(expYear: string): ValidationErrors | null {
        console.log(expYear);
        if (this.evaluateCreationYear(expYear)) {
            console.log('BIEN');
            return null;
        } else {
            console.log('MAL');
            return {
                pastYear: {
                    valid: false
                }
            };
        }
    }

    evaluateCreationYear(expYear: string): boolean {
        if (expYear === '') {
            return false;
        }
        if (isNaN(Number(expYear)) || expYear.includes(',') || expYear.includes('.')) {
            return false;
        }
        console.log('HILA');
        const now: Date = new Date(Date.now());
        const year: Number = new Number(expYear);
        console.log(now.getFullYear());
        if (now.getFullYear() < year) {
            return false;
        } else {
            return true;
        }
    }
}
