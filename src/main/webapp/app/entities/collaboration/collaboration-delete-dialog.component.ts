import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICollaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';

@Component({
    selector: 'jhi-collaboration-delete-dialog',
    templateUrl: './collaboration-delete-dialog.component.html'
})
export class CollaborationDeleteDialogComponent {
    collaboration: ICollaboration;

    constructor(
        protected collaborationService: CollaborationService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.collaborationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'collaborationListModification',
                content: 'Deleted an collaboration'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-collaboration-delete-popup',
    template: ''
})
export class CollaborationDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ collaboration }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CollaborationDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.collaboration = collaboration;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/collaboration', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/collaboration', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
