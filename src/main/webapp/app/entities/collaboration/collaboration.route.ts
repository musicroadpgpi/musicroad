import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Collaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';
import { CollaborationComponent } from './collaboration.component';
import { CollaborationDetailComponent } from './collaboration-detail.component';
import { CollaborationUpdateComponent } from './collaboration-update.component';
import { CollaborationDeletePopupComponent } from './collaboration-delete-dialog.component';
import { ICollaboration } from 'app/shared/model/collaboration.model';

@Injectable({ providedIn: 'root' })
export class CollaborationResolve implements Resolve<ICollaboration> {
    constructor(private service: CollaborationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICollaboration> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Collaboration>) => response.ok),
                map((collaboration: HttpResponse<Collaboration>) => collaboration.body)
            );
        }
        return of(new Collaboration());
    }
}

export const collaborationRoute: Routes = [
    {
        path: '',
        component: CollaborationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'musicroadApp.collaboration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'my-collaborations',
        component: CollaborationComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'musicroadApp.collaboration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: CollaborationDetailComponent,
        resolve: {
            collaboration: CollaborationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'musicroadApp.collaboration.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: CollaborationUpdateComponent,
        resolve: {
            collaboration: CollaborationResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'musicroadApp.collaboration.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
    // {
    //     path: ':id/edit',
    //     component: CollaborationUpdateComponent,
    //     resolve: {
    //         collaboration: CollaborationResolve
    //     },
    //     data: {
    //         authorities: ['ROLE_USER'],
    //         pageTitle: 'musicroadApp.collaboration.home.title'
    //     },
    //     canActivate: [UserRouteAccessService]
    // }
];

export const collaborationPopupRoute: Routes = [
    // {
    // path: ':id/delete',
    // component: CollaborationDeletePopupComponent,
    // resolve: {
    //     collaboration: CollaborationResolve
    // },
    // data: {
    //     authorities: ['ROLE_USER'],
    //     pageTitle: 'musicroadApp.collaboration.home.title'
    // },
    // canActivate: [UserRouteAccessService],
    // outlet: 'popup'
    // }
];
