import { Route } from '@angular/router';

import { UserRouteAccessService } from 'app/core';
import { PaymentComponent } from './payment.component';

export const paymentRoute: Route = {
    path: 'payment',
    component: PaymentComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.payment'
    },
    canActivate: [UserRouteAccessService]
};
