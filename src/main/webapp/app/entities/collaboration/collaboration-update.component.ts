import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ICollaboration, Collaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';
import { IBand } from 'app/shared/model/band.model';
import { BandService } from 'app/entities/band';
import { IUser, AccountService } from 'app/core';

@Component({
    selector: 'jhi-collaboration-update',
    templateUrl: './collaboration-update.component.html'
})
export class CollaborationUpdateComponent implements OnInit {
    @Input() collaboration: ICollaboration;
    isSaving: boolean;

    bands: IBand[];
    proposedDateDp: any;

    @Input() showingBandDetails: boolean;
    user: IUser;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected collaborationService: CollaborationService,
        protected bandService: BandService,
        protected accountService: AccountService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        if (this.showingBandDetails !== true) {
            this.showingBandDetails = false;
        }
        this.isSaving = false;
        if (this.user === undefined) {
            this.accountService.fetch().subscribe((principalResponse: HttpResponse<IUser>) => {
                this.user = principalResponse.body;
                if (this.showingBandDetails) {
                    this.bands = [];
                    this.activatedRoute.data.subscribe(({ band }) => {
                        this.bands.push(band);
                    });
                    this.bandService
                        .search({ query: 'user.login.equals=' + this.user.login })
                        .subscribe((searchBandResponse: HttpResponse<IBand[]>) => {
                            searchBandResponse.body.forEach((band: IBand) => {
                                this.bands.push(band);
                            });
                        });
                    if (this.collaboration === undefined) {
                        this.collaboration = new Collaboration();
                    }
                }
            });
        }
        if (!this.showingBandDetails) {
            this.activatedRoute.data.subscribe(({ collaboration }) => {
                this.collaboration = collaboration;
            });
            this.bandService
                .query()
                .pipe(
                    filter((mayBeOk: HttpResponse<IBand[]>) => mayBeOk.ok),
                    map((response: HttpResponse<IBand[]>) => response.body)
                )
                .subscribe((res: IBand[]) => (this.bands = res), (res: HttpErrorResponse) => this.onError(res.message));
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.collaboration.id !== undefined) {
            this.subscribeToSaveResponse(this.collaborationService.update(this.collaboration));
        } else {
            this.subscribeToSaveResponse(this.collaborationService.create(this.collaboration));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICollaboration>>) {
        result.subscribe(
            (res: HttpResponse<ICollaboration>) => {
                this.onSaveSuccess();
                this.bands.forEach((band: IBand) => {
                    band.collaborations.push(res.body);
                    this.bandService.update(band).subscribe((savedBandResponse: HttpResponse<IBand>) => {});
                });
            },
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackBandById(index: number, item: IBand) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
