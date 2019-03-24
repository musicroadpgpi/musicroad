import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
@Component({
    selector: 'jhi-methodology',
    templateUrl: './methodology.component.html',
    styles: []
})
export class MethodologyComponent implements OnInit {
    constructor(private titleService: Title) {}

    ngOnInit() {
        this.titleService.setTitle('Methodology');
    }
}
