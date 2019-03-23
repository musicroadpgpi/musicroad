import { Moment } from 'moment';
import { IBand } from 'app/shared/model/band.model';

export interface ICollaboration {
    id?: number;
    message?: string;
    proposedDate?: Moment;
    bands?: IBand[];
}

export class Collaboration implements ICollaboration {
    constructor(public id?: number, public message?: string, public proposedDate?: Moment, public bands?: IBand[]) {}
}
