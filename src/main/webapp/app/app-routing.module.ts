import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute, navbarRoute } from './layouts';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { TeamComponent } from './team/team.component';
import { SearcherComponent } from './searcher/searcher.component';
import { MethodologyComponent } from './methodology/methodology.component';
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
                ...LAYOUT_ROUTES
            ],
            { useHash: true, enableTracing: DEBUG_INFO_ENABLED }
        )
    ],
    exports: [RouterModule]
})
export class MusicroadAppRoutingModule {}
