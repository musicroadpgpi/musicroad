import { IUser } from 'app/core/user/user.model';
import { ICity } from 'app/shared/model/city.model';
import { ICollaboration } from 'app/shared/model/collaboration.model';

export const enum Genre {
    Rock = 'Rock',
    RandB = 'RandB',
    Soul = 'Soul',
    Pop = 'Pop',
    Jazz = 'Jazz',
    Folk = 'Folk',
    Electronic = 'Electronic',
    Country = 'Country',
    Blues = 'Blues',
    Flamenco = 'Flamenco',
    Rap = 'Rap',
    Reggaeton = 'Reggaeton',
    Reggae = 'Reggae',
    HeavyMetal = 'HeavyMetal',
    HardRock = 'HardRock',
    Classic = 'Classic',
    Dance = 'Dance',
    AlternativeRock = 'AlternativeRock',
    PopLatino = 'PopLatino',
    Punk = 'Punk',
    Techno = 'Techno',
    Grunge = 'Grunge',
    House = 'House',
    IndieRock = 'IndieRock',
    Funk = 'Funk'
}

export interface IBand {
    id?: number;
    bandName?: string;
    bio?: string;
    coverPicture?: string;
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
        public bio?: string,
        public coverPicture?: string,
        public componentNumber?: number,
        public creationYear?: number,
        public genre?: Genre,
        public user?: IUser,
        public city?: ICity,
        public collaborations?: ICollaboration[]
    ) {}
}
