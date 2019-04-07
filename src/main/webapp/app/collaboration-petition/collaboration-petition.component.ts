import { Component, OnInit, SystemJsNgModuleLoader } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BandService } from 'app/entities/band/band.service';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Title } from '@angular/platform-browser';
import { IBand } from 'app/shared/model/band.model';
import { CollaborationService } from 'app/entities/collaboration';
import { User, AccountService } from 'app/core';
import { ICollaboration, Collaboration } from 'app/shared/model/collaboration.model';

@Component({
    selector: 'jhi-collaboration-petition',
    templateUrl: './collaboration-petition.component.html',
    styles: []
})
export class CollaborationPetitionComponent implements OnInit {
    currentSearch: string;
    page: any;
    itemsPerPage: number;
    predicate: any;
    reverse: any;
    links: any;
    totalItems: number;
    title = 'app';
    user: User;
    band: IBand;
    bands: IBand[] = [];
    collaboration: ICollaboration;

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected bandService: BandService,
        protected collaborationService: CollaborationService,
        protected accountService: AccountService,
        protected client: HttpClient,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        private titleService: Title
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['collaboration-petition']
                ? this.activatedRoute.snapshot.params['collaboration-petition']
                : '';
        this.activatedRoute.data.subscribe(({ band }) => {
            this.band = band;
            this.accountService.fetch().subscribe((responseUser: HttpResponse<User>) => {
                this.user = responseUser.body;
                this.bandService
                    .search({ query: 'user.login.equals=' + this.user.login })
                    .subscribe((responseBand: HttpResponse<IBand[]>) => {
                        responseBand.body.forEach((band1: IBand) => {
                            this.bands.push(band1);
                        });
                        this.bands.push(this.band);
                        if (this.collaboration === undefined) {
                            this.collaboration = new Collaboration();
                            this.collaboration.bands = this.bands;
                        }
                    });
            });
        });
    }

    //   ngOnInit() {

    //     if (this.currentSearch) {
    //         this.bandService
    //             .search({
    //                 query: this.currentSearch,
    //                 page: this.page,
    //                 size: this.itemsPerPage,
    //                 sort: this.sort()
    //             })
    //             .subscribe(
    //                 (res: HttpResponse<IBand[]>) => this.paginateBands(res.body, res.headers),
    //                 (res: HttpErrorResponse) => this.onError(res.message)
    //             );
    //         return;
    //     }
    //     this.bandService
    //         .query({
    //             page: this.page,
    //             size: this.itemsPerPage,
    //             sort: this.sort()
    //         })
    //         .subscribe(
    //             (res: HttpResponse<IBand[]>) => this.paginateBands(res.body, res.headers),
    //             (res: HttpErrorResponse) => this.onError(res.message)
    //         );
    //   }

    ngOnInit() {
        this.titleService.setTitle('Collaboration petition');
        this.bands.push(this.band);
        // Esto es para tener un usuario con el que hacer las comprobaciones de seguridad (4 líneas)
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
