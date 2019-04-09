import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICollaboration } from 'app/shared/model/collaboration.model';
import { BandService } from '../band';
import { AccountService, IUser } from 'app/core';
import { IBand } from 'app/shared/model/band.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-collaboration-detail',
    templateUrl: './collaboration-detail.component.html'
})
export class CollaborationDetailComponent implements OnInit {
    collaboration: ICollaboration;
    user: IUser;

    constructor(protected activatedRoute: ActivatedRoute, protected bandService: BandService, protected accountService: AccountService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ collaboration }) => {
            this.collaboration = collaboration;
        });
        this.accountService.fetch().subscribe((responseFetch: HttpResponse<IUser>) => {
            this.user = responseFetch.body;
            this.bandService
                .search({ query: 'id.equals=' + this.collaboration.id })
                .subscribe((responseSearchBand: HttpResponse<IBand[]>) => {
                    this.collaboration.bands = responseSearchBand.body.filter((filtBand, filtInd, filtBands) => {
                        return filtBand.collaborations.some((someCollab, someInd, someCollabs) => {
                            if (someCollab !== null) {
                                return someCollab.id === this.collaboration.id;
                            } else {
                                return false;
                            }
                        });
                    });
                    console.log(this.collaboration.bands);
                });
        });
    }

    previousState() {
        window.history.back();
    }
}
