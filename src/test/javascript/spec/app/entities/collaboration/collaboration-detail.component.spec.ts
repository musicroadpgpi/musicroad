/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MusicroadTestModule } from '../../../test.module';
import { CollaborationDetailComponent } from 'app/entities/collaboration/collaboration-detail.component';
import { Collaboration } from 'app/shared/model/collaboration.model';

describe('Component Tests', () => {
    describe('Collaboration Management Detail Component', () => {
        let comp: CollaborationDetailComponent;
        let fixture: ComponentFixture<CollaborationDetailComponent>;
        const route = ({ data: of({ collaboration: new Collaboration(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MusicroadTestModule],
                declarations: [CollaborationDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CollaborationDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CollaborationDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.collaboration).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
