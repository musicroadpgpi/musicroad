import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd, RouterEvent } from '@angular/router';

import { ICollaboration } from 'app/shared/model/collaboration.model';
import { BandService } from '../band';
import { AccountService, IUser } from 'app/core';
import { IBand, Band } from 'app/shared/model/band.model';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-collaboration-detail',
    templateUrl: './collaboration-detail.component.html'
})
export class CollaborationDetailComponent implements OnInit {
    collaboration: ICollaboration;
    user: IUser;
    userBand: IBand;
    idOtherBand: number;

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected bandService: BandService,
        protected accountService: AccountService,
        protected router: Router
    ) {}

    ngOnInit() {
        this.router.events.subscribe((event: RouterEvent) => {
            if (event instanceof NavigationEnd) {
                this.update();
            }
        });
        this.start();
    }

    start() {
        this.activatedRoute.data.subscribe(({ collaboration }) => {
            this.collaboration = collaboration;
            console.log(collaboration);
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
                    this.collaboration.bands.forEach((band: IBand) => {
                        if (band.user.id !== this.user.id) {
                            this.idOtherBand = band.id;
                        }
                    });
                    this.bandService
                        .search({ query: 'user.login.equals=' + this.user.login })
                        .subscribe((responseSearchPrincipalBand: HttpResponse<IBand[]>) => {
                            this.userBand = responseSearchPrincipalBand.body[0];
                            this.collaboration.bands.forEach((band: IBand) => {
                                if (this.userBand.id === band.id) {
                                    this.userBand = band;
                                }
                            });
                        });
                });
        });
    }

    update() {
        this.activatedRoute.data.subscribe(({ collaboration }) => {
            this.collaboration = collaboration;
            console.log(collaboration);
        });
        this.bandService.search({ query: 'id.equals=' + this.collaboration.id }).subscribe((responseSearchBand: HttpResponse<IBand[]>) => {
            this.collaboration.bands = responseSearchBand.body.filter((filtBand, filtInd, filtBands) => {
                return filtBand.collaborations.some((someCollab, someInd, someCollabs) => {
                    if (someCollab !== null) {
                        return someCollab.id === this.collaboration.id;
                    } else {
                        return false;
                    }
                });
            });
            this.collaboration.bands.forEach((band: IBand) => {
                if (band.user.id !== this.user.id) {
                    this.idOtherBand = band.id;
                }
            });
            this.collaboration.bands.forEach((band: IBand) => {
                if (this.userBand.id === band.id) {
                    this.userBand = band;
                }
            });
        });
    }

    previousState() {
        window.history.back();
    }

    getBands() {
        this.bandService.search({ query: 'id.equals=' + this.collaboration.id }).subscribe((responseSearchBand: HttpResponse<IBand[]>) => {
            this.collaboration.bands = responseSearchBand.body.filter((filtBand, filtInd, filtBands) => {
                return filtBand.collaborations.some((someCollab, someInd, someCollabs) => {
                    if (someCollab !== null) {
                        return someCollab.id === this.collaboration.id;
                    } else {
                        return false;
                    }
                });
            });
        });
    }
}
