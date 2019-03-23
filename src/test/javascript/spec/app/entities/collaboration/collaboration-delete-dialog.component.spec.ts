/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { MusicroadTestModule } from '../../../test.module';
import { CollaborationDeleteDialogComponent } from 'app/entities/collaboration/collaboration-delete-dialog.component';
import { CollaborationService } from 'app/entities/collaboration/collaboration.service';

describe('Component Tests', () => {
    describe('Collaboration Management Delete Component', () => {
        let comp: CollaborationDeleteDialogComponent;
        let fixture: ComponentFixture<CollaborationDeleteDialogComponent>;
        let service: CollaborationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MusicroadTestModule],
                declarations: [CollaborationDeleteDialogComponent]
            })
                .overrideTemplate(CollaborationDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CollaborationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CollaborationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
