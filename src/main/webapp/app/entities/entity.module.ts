import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'city',
                loadChildren: './city/city.module#MusicroadCityModule'
            },
            {
                path: 'band',
                loadChildren: './band/band.module#MusicroadBandModule'
            },
            {
                path: 'collaboration',
                loadChildren: './collaboration/collaboration.module#MusicroadCollaborationModule'
            },
            {
                path: 'band',
                loadChildren: './band/band.module#MusicroadBandModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MusicroadEntityModule {}
