import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICollaboration } from 'app/shared/model/collaboration.model';

type EntityResponseType = HttpResponse<ICollaboration>;
type EntityArrayResponseType = HttpResponse<ICollaboration[]>;

@Injectable({ providedIn: 'root' })
export class CollaborationService {
    public resourceUrl = SERVER_API_URL + 'api/collaborations';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/collaborations';

    constructor(protected http: HttpClient) {}

    create(collaboration: ICollaboration): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(collaboration);
        console.log(collaboration.bands);
        console.log(copy.bands);
        return this.http
            .post<ICollaboration>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(collaboration: ICollaboration): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(collaboration);
        return this.http
            .put<ICollaboration>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ICollaboration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICollaboration[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ICollaboration[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(collaboration: ICollaboration): ICollaboration {
        const copy: ICollaboration = Object.assign({}, collaboration, {
            proposedDate:
                collaboration.proposedDate != null && collaboration.proposedDate.isValid()
                    ? collaboration.proposedDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.proposedDate = res.body.proposedDate != null ? moment(res.body.proposedDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((collaboration: ICollaboration) => {
                collaboration.proposedDate = collaboration.proposedDate != null ? moment(collaboration.proposedDate) : null;
            });
        }
        return res;
    }
}
