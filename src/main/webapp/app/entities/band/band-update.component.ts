import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute, UrlSegment } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { IBand, Band } from 'app/shared/model/band.model';
import { BandService } from './band.service';
import { IUser, UserService, AccountService } from 'app/core';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city';
import { ICollaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from 'app/entities/collaboration';

@Component({
    selector: 'jhi-band-update',
    templateUrl: './band-update.component.html'
})
export class BandUpdateComponent implements OnInit {
    band: IBand;
    isSaving: boolean;

    users: IUser[];

    cities: ICity[];

    collaborations: ICollaboration[];

    user: IUser;
    canEdit = false;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected bandService: BandService,
        protected userService: UserService,
        protected cityService: CityService,
        protected collaborationService: CollaborationService,
        protected accountService: AccountService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ band }) => {
            console.log(band);
            this.accountService.fetch().subscribe((fetchResponse: HttpResponse<IUser>) => {
                this.user = fetchResponse.body;
                this.activatedRoute.url.subscribe((urlSegments: UrlSegment[]) => {
                    this.bandService
                        .search({ query: 'login.equals=' + fetchResponse.body.login })
                        .subscribe((searchBandResponse: HttpResponse<IBand[]>) => {
                            console.log('searchRes: ' + searchBandResponse.body[0]);
                            if (searchBandResponse.body[0] !== undefined) {
                                console.log('NOOOOOOOOOOOOO');
                                this.band = searchBandResponse.body[0];
                            }
                            const inEditMyBand: boolean = urlSegments.some((urlSegment: UrlSegment) => {
                                if (urlSegment.path.includes('edit-my-band')) {
                                    return true;
                                } else {
                                    return false;
                                }
                            });
                            if (inEditMyBand) {
                                this.canEdit = true;
                            } else {
                                if (this.band !== undefined) {
                                    if (this.band.user !== undefined) {
                                        if (this.band.user.id === this.user.id) {
                                            this.canEdit = true;
                                        }
                                    }
                                }
                            }
                            if (this.band === undefined) {
                                this.band = band;
                            }
                            console.log('Finally : ' + this.band);
                        });
                });
            });
        });
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.cityService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICity[]>) => response.body)
            )
            .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.collaborationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICollaboration[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICollaboration[]>) => response.body)
            )
            .subscribe((res: ICollaboration[]) => (this.collaborations = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.band.id !== undefined) {
            this.subscribeToSaveResponse(this.bandService.update(this.band));
        } else {
            this.subscribeToSaveResponse(this.bandService.create(this.band));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBand>>) {
        result.subscribe((res: HttpResponse<IBand>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCityById(index: number, item: ICity) {
        return item.id;
    }

    trackCollaborationById(index: number, item: ICollaboration) {
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
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.band, this.elementRef, field, fieldContentType, idInput);
    }
}
