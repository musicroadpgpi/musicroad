import { Component, OnInit, ElementRef, Renderer } from '@angular/core';
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
import { thisExpression } from '@babel/types';
import { YEAR_ERROR, IMAGE_ERROR, NUMBER_ERROR } from 'app/shared';

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
    success: any;
    errorYear: string;
    errorImage: string;
    errorCNumber: string;
    error: string;
    load: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected bandService: BandService,
        protected userService: UserService,
        protected cityService: CityService,
        protected collaborationService: CollaborationService,
        protected accountService: AccountService,
        protected elementRef: ElementRef,
        protected elementRef2: ElementRef,
        protected activatedRoute: ActivatedRoute,
        private renderer: Renderer
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ band }) => {
            this.accountService.fetch().subscribe((fetchResponse: HttpResponse<IUser>) => {
                this.user = fetchResponse.body;
                this.activatedRoute.url.subscribe((urlSegments: UrlSegment[]) => {
                    if (band === undefined) {
                        this.band = new Band();
                        this.band.user = this.user;
                    } else {
                        this.bandService
                            .search({ query: 'login.equals=' + fetchResponse.body.login })
                            .subscribe((searchBandResponse: HttpResponse<IBand[]>) => {
                                if (searchBandResponse.body[0] !== undefined) {
                                    this.band = searchBandResponse.body[0];
                                    console.log(this.band);
                                } else {
                                    this.band = new Band();
                                    this.band.user = this.user;
                                }
                            });
                    }
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
    up() {
        this.renderer.invokeElementMethod(this.elementRef2.nativeElement.querySelector('#field_bandName'), 'focus', []);
    }

    save() {
        this.load = true;
        this.up();
        this.isSaving = true;
        this.accountService.fetch().subscribe((response: HttpResponse<IUser>) => {
            this.band.user = response.body;
            if (this.band.id !== undefined) {
                this.subscribeToSaveResponse(this.bandService.update(this.band));
            } else {
                this.subscribeToSaveResponse(this.bandService.create(this.band));
            }
        });
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IBand>>) {
        result.subscribe((res: HttpResponse<IBand>) => this.onSaveSuccess(), response => this.processError(response));
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.load = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    private processError(response: HttpErrorResponse) {
        this.isSaving = false;
        this.load = false;
        if (response.status === 400 && response.error.type === YEAR_ERROR) {
            this.errorYear = 'ERROR';
        } else if (response.status === 400 && response.error.type === IMAGE_ERROR) {
            this.errorImage = 'ERROR';
        } else if (response.status === 400 && response.error.type === NUMBER_ERROR) {
            this.errorCNumber = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
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
