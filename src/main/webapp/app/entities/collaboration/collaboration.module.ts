import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { MusicroadSharedModule } from 'app/shared';
import {
    CollaborationComponent,
    CollaborationDetailComponent,
    CollaborationUpdateComponent,
    CollaborationDeletePopupComponent,
    CollaborationDeleteDialogComponent,
    CollaborationCreateComponent,
    collaborationRoute,
    collaborationPopupRoute
} from './';

const ENTITY_STATES = [...collaborationRoute, ...collaborationPopupRoute];

@NgModule({
    imports: [MusicroadSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CollaborationComponent,
        CollaborationDetailComponent,
        CollaborationUpdateComponent,
        CollaborationDeleteDialogComponent,
        CollaborationCreateComponent,
        CollaborationDeletePopupComponent
    ],
    entryComponents: [
        CollaborationComponent,
        CollaborationUpdateComponent,
        CollaborationDeleteDialogComponent,
        CollaborationDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    exports: [CollaborationUpdateComponent]
})
export class MusicroadCollaborationModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
