import { IUser } from 'app/core/user/user.model';
import { ICity } from 'app/shared/model/city.model';
import { ICollaboration } from 'app/shared/model/collaboration.model';

export const enum Genre {
    Rock = 'Rock',
    RandB = 'RandB',
    Soul = 'Soul',
    Pop = 'Pop',
    Latin = 'Latin',
    Jazz = 'Jazz',
    HipHop = 'HipHop',
    Folk = 'Folk',
    Electronic = 'Electronic',
    Country = 'Country',
    Blues = 'Blues',
    Flamenco = 'Flamenco'
}

export interface IBand {
    id?: number;
    bandName?: string;
    componentNumber?: number;
    creationYear?: number;
    genre?: Genre;
    user?: IUser;
    city?: ICity;
    collaborations?: ICollaboration[];
}

export class Band implements IBand {
    constructor(
        public id?: number,
        public bandName?: string,
        public componentNumber?: number,
        public creationYear?: number,
        public genre?: Genre,
        public user?: IUser,
        public city?: ICity,
        public collaborations?: ICollaboration[]
    ) {}
}
