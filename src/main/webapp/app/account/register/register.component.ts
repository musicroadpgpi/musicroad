import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService, JhiDataUtils } from 'ng-jhipster';
import { IBand } from 'app/shared/model/band.model';
import { ICity } from 'app/shared/model/city.model';
import { ActivatedRoute } from '@angular/router';
import { CityService } from 'app/entities/city';
import {
    EMAIL_ALREADY_USED_TYPE,
    LOGIN_ALREADY_USED_TYPE,
    INVALID_PASSWORD_TYPE,
    CONSTRAINT_VIOLATION_TYPE,
    YEAR_ERROR,
    IMAGE_ERROR,
    BANDNAME_ERROR
} from 'app/shared';
import { LoginModalService } from 'app/core';
import { Register } from './register.service';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-register',
    templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
    confirmPassword: string;
    doNotMatch: string;
    error: string;
    errorEmailExists: string;
    errorUserExists: string;
    errorBandName: string;
    errorYear: string;
    registerAccount: any;
    success: boolean;
    modalRef: NgbModalRef;
    cities: ICity[];
    band: IBand;
    errorImage: string;
    constructor(
        protected dataUtils: JhiDataUtils,
        protected elementRef2: ElementRef,
        protected jhiAlertService: JhiAlertService,
        private languageService: JhiLanguageService,
        private loginModalService: LoginModalService,
        protected cityService: CityService,
        private registerService: Register,
        private elementRef: ElementRef,
        private renderer: Renderer,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ band }) => {
            this.band = band;
        });

        this.success = false;
        this.registerAccount = {};
        this.cityService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICity[]>) => response.body)
            )
            .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    ngAfterViewInit() {
        this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
    }

    register() {
        if (this.registerAccount.password !== this.confirmPassword) {
            this.doNotMatch = 'ERROR';
        } else {
            this.doNotMatch = null;
            this.error = null;
            this.errorUserExists = null;
            this.errorEmailExists = null;
            this.errorBandName = null;
            this.errorYear = null;
            this.errorImage = null;
            this.languageService.getCurrent().then(key => {
                this.registerAccount.langKey = key;
                this.registerService.save(this.registerAccount).subscribe(
                    () => {
                        this.success = true;
                    },
                    response => this.processError(response)
                );
            });
        }
    }

    openLogin() {
        this.modalRef = this.loginModalService.open();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    trackCityById(index: number, item: ICity) {
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
    private processError(response: HttpErrorResponse) {
        this.success = null;
        if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
            this.errorUserExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
            this.errorEmailExists = 'ERROR';
        } else if (response.status === 400 && response.error.type === YEAR_ERROR) {
            this.errorYear = 'ERROR';
        } else if (response.status === 400 && response.error.type === IMAGE_ERROR) {
            this.errorImage = 'ERROR';
        } else {
            this.error = 'ERROR';
        }
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
        this.dataUtils.clearInputImage(this.registerAccount, this.elementRef2, field, fieldContentType, idInput);
    }
}
