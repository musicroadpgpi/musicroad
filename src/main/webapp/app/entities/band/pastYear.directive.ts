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
        if (this.evaluateCreationYear(expYear)) {
            return null;
        } else {
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
        const now: Date = new Date(Date.now());
        const year: Number = new Number(expYear);
        if (now.getFullYear() < year) {
            return false;
        } else {
            return true;
        }
    }
}
