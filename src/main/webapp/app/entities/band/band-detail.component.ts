import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { User, AccountService } from 'app/core';
import { IBand } from 'app/shared/model/band.model';
import { JhiDataUtils } from 'ng-jhipster';

@Component({
    selector: 'jhi-band-detail',
    templateUrl: './band-detail.component.html'
})
export class BandDetailComponent implements OnInit {
    band: IBand;
    @Input() user: User;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ band }) => {
            this.band = band;
        });
        // Esto es para tener un usuario con el que hacer las comprobaciones de seguridad (4 líneas)
        if (this.user === undefined) {
            this.accountService.fetch().subscribe((response: HttpResponse<User>) => {
                this.user = response.body;
            });
        }
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
