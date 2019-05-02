import { Component, OnInit, SystemJsNgModuleLoader } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BandService } from 'app/entities/band/band.service';
import { CityService } from 'app/entities/city/city.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse, HttpClient } from '@angular/common/http';
import { JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { Title } from '@angular/platform-browser';
import { IBand } from 'app/shared/model/band.model';
import { ICity, City } from 'app/shared/model/city.model';
import { SERVER_API_URL } from 'app/app.constants';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ITEMS_PER_PAGE } from 'app/shared';
import { IUser, AccountService } from 'app/core';

@Component({
    selector: 'jhi-searcher',
    templateUrl: './searcher.component.html',
    styles: []
})
export class SearcherComponent implements OnInit {
    currentSearch: string;
    page: any;
    itemsPerPage: number;
    predicate: any;
    reverse: any;
    links: any;
    totalItems: number;
    title = 'app';
    genres = [
        'Pop',
        'Electronic',
        'Rock',
        'Rap',
        'Reggaeton',
        'Reggae',
        'HeavyMetal',
        'HardRock',
        'Classic',
        'Dance',
        'Blues',
        'AlternativeRock',
        'Jazz',
        'PopLatino',
        'Punk',
        'Techno',
        'Grunge',
        'House',
        'IndieRock',
        'Flamenco',
        'RandB',
        'Country',
        'Folk',
        'Soul',
        'Funk'
    ];
    // cities: string[] = ['Madrid' , 'Sevilla' , 'Malaga' , 'Huelva' , 'Valladolid' , 'Granada' , 'Barcelona' , 'Jaén' , 'A Coruña' , 'Guadalajara' , 'Huesca' , 'Lugo' , 'La Rioja' , 'Gipuzkoa' , 'Pontevedra' , 'Teruel'];
    cities: ICity[] = [];
    city: City;
    bands: IBand[] = [];
    cityes: ICity[];
    formSearch: FormGroup;
    fromSearcher = true;
    user: IUser;

    constructor(
        protected activatedRoute: ActivatedRoute,
        protected bandService: BandService,
        protected cityService: CityService,
        protected client: HttpClient,
        protected parseLinks: JhiParseLinks,
        protected jhiAlertService: JhiAlertService,
        protected accountService: AccountService,
        private titleService: Title
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
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
        const fBuilder: FormBuilder = new FormBuilder();
        this.formSearch = fBuilder.group({
            genre: [''],
            city: ['']
        });
        this.client.get(SERVER_API_URL.concat('/api/cities')).subscribe((cityes: ICity[]) => {
            cityes.forEach(element => {
                this.cities[this.cities.length] = element;
            });
        });
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.accountService.fetch().subscribe((response: HttpResponse<IUser>) => {
            this.user = response.body;
            this.loadAll();
        });
        this.titleService.setTitle('Search');
    }

    loadAll() {
        let resultBands: IBand[] = [];
        if (this.currentSearch) {
            this.bandService
                .search({
                    query: this.currentSearch,
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<IBand[]>) => {
                        resultBands = res.body.filter((filterBand: IBand) => {
                            if (filterBand.user.id === this.user.id) {
                                return false;
                            } else {
                                return true;
                            }
                        });
                        this.paginateBands(resultBands, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.bandService
            .query({
                page: this.page,
                size: this.itemsPerPage,
                sort: this.sort()
            })
            .subscribe(
                (res: HttpResponse<IBand[]>) => {
                    resultBands = res.body.filter((filterBand: IBand) => {
                        if (filterBand.user.id === this.user.id) {
                            return false;
                        } else {
                            return true;
                        }
                    });
                    this.paginateBands(resultBands, res.headers);
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    clear() {
        this.bands = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.bands = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    onSubmit() {
        let query = '';
        const cityName: string = this.formSearch.get('city').value;
        const genre: string = this.formSearch.get('genre').value;
        if (cityName !== '' && genre !== '') {
            query = 'genre.equals=' + genre + ' AND ' + 'name.equals=' + cityName;
        } else if (cityName !== '' && genre === '') {
            query = 'name.equals=' + cityName;
        } else if (cityName === '' && genre !== '') {
            query = 'genre.equals=' + genre;
        }
        console.log(query);
        this.search(query);
    }

    protected paginateBands(data: IBand[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.bands.push(data[i]);
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
