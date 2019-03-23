export interface ICity {
    id?: number;
    name?: string;
    country?: string;
}

export class City implements ICity {
    constructor(public id?: number, public name?: string, public country?: string) {}
}
