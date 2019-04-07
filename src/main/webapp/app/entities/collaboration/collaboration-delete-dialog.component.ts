import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICollaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';

import { BandService } from 'app/entities/band/band.service';
import { IBand } from 'app/shared/model/band.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-collaboration-delete-dialog',
    templateUrl: './collaboration-delete-dialog.component.html'
})
export class CollaborationDeleteDialogComponent {
    collaboration: ICollaboration;

    constructor(
        protected collaborationService: CollaborationService,
        protected bandService: BandService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bandService.search({ query: 'id.equals=' + id }).subscribe((searchBandsResponse: HttpResponse<IBand[]>) => {
            let count = searchBandsResponse.body.length;
            searchBandsResponse.body.forEach((band: IBand) => {
                delete band.collaborations[
                    band.collaborations.findIndex((collab: ICollaboration, ind: number, collabs: ICollaboration[]) => {
                        let res = false;
                        if (collabs[ind] !== null) {
                            if (collabs[ind].id === id) {
                                res = true;
                            }
                        }
                        return res;
                    })
                ];
                this.bandService.update(band).subscribe((updateBandResponse: HttpResponse<IBand>) => {
                    count--;
                    if (count === 0) {
                        this.collaborationService.delete(id).subscribe(response => {
                            this.eventManager.broadcast({
                                name: 'collaborationListModification',
                                content: 'Deleted an collaboration'
                            });
                            this.activeModal.dismiss(true);
                        });
                    }
                });
            });
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
