import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { TeamComponent } from './team/team.component';
import { SearcherComponent } from './searcher/searcher.component';
import { MethodologyComponent } from './methodology/methodology.component';
import { CollaborationPetitionComponent } from './collaboration-petition/collaboration-petition.component';
import { BandResolve } from './entities/band/band.route';
import { UserRouteAccessService } from 'app/core';
import { GpdrComponent } from './gpdr/gpdr.component';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
    imports: [
        RouterModule.forRoot(
            [
                {
                    path: 'admin',
                    loadChildren: './admin/admin.module#MusicroadAdminModule'
                },
                {
                    path: 'team',
                    component: TeamComponent
                },
                {
                    path: 'methodology',
                    component: MethodologyComponent
                },
                {
                    path: 'searcher',
                    component: SearcherComponent
                },
                {
                    path: 'gpdr',
                    component: GpdrComponent
                },
                {
                    path: 'collaboration-petition/:id/view',
                    component: CollaborationPetitionComponent,
                    resolve: {
                        band: BandResolve
                    },
                    data: {
                        authorities: ['ROLE_USER'],
                        pageTitle: 'musicroadApp.band.home.title'
                    },
                    canActivate: [UserRouteAccessService]
                },
                ...LAYOUT_ROUTES
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class MusicroadAppRoutingModule {}
