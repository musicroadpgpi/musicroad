import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MusicroadSharedModule } from 'app/shared';
import {
    BandComponent,
    BandDetailComponent,
    BandUpdateComponent,
    BandDeletePopupComponent,
    BandDeleteDialogComponent,
    bandRoute,
    bandPopupRoute
} from './';

const ENTITY_STATES = [...bandRoute, ...bandPopupRoute];

@NgModule({
    imports: [MusicroadSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [BandComponent, BandDetailComponent, BandUpdateComponent, BandDeleteDialogComponent, BandDeletePopupComponent],
    entryComponents: [BandComponent, BandUpdateComponent, BandDeleteDialogComponent, BandDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [BandComponent]
})
export class MusicroadBandModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
