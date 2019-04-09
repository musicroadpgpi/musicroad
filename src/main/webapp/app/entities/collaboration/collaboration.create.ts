import { Component } from '@angular/core';
import { ICollaboration } from 'app/shared/model/collaboration.model';
import { CollaborationService } from './collaboration.service';

@Component({
    selector: 'jhi-collaboration-create',
    templateUrl: './collaboration.create.html'
})
export class CollaborationCreate {
    collaboration: ICollaboration;
    constructor(protected collaborationService: CollaborationService) {
        //this.collaboration.proposedDate = (document.getElementById('dateInput'), 'Moment');
        collaborationService.create;
    }
}
