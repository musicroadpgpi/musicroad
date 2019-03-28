import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';

import { User, AccountService } from 'app/core';
import { IBand } from 'app/shared/model/band.model';

@Component({
    selector: 'jhi-band-detail',
    templateUrl: './band-detail.component.html'
})
export class BandDetailComponent implements OnInit {
    band: IBand;
    user: User;

    constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ band }) => {
            this.band = band;
        });
        // Esto es para tener un usuario con el que hacer las comprobaciones de seguridad (4 líneas)
        this.accountService.fetch().subscribe((response: HttpResponse<User>) => {
            this.user = response.body;
        });
    }

    previousState() {
        window.history.back();
    }
}
