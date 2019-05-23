import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { PastYearValidator } from './pastYear.directive';

import { MusicroadSharedModule } from 'app/shared';

import {
    PasswordStrengthBarComponent,
    RegisterComponent,
    ActivateComponent,
    PasswordComponent,
    PasswordResetInitComponent,
    PasswordResetFinishComponent,
    SettingsComponent,
    accountState
} from './';
import { PaymentComponent } from './payment/payment.component';

@NgModule({
    imports: [MusicroadSharedModule, RouterModule.forChild(accountState)],
    declarations: [
        ActivateComponent,
        RegisterComponent,
        PasswordComponent,
        PasswordStrengthBarComponent,
        PasswordResetInitComponent,
        PasswordResetFinishComponent,
        SettingsComponent,
        PaymentComponent,
        PastYearValidator
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MusicroadAccountModule {}
