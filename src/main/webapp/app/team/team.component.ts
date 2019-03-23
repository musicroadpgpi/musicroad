import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';
@Component({
    selector: 'jhi-team',
    templateUrl: './team.component.html',
    styles: []
})
export class TeamComponent implements OnInit {
    constructor(private titleService: Title) {}
    ngOnInit() {
        this.titleService.setTitle('Team members');
    }
}
