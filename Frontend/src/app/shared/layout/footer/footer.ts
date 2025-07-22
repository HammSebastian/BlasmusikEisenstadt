import {Component, Input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {Constants} from '../Constants';

@Component({
  selector: 'app-footer',
    imports: [
        RouterLink
    ],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class Footer {
    @Input() currentYear = new Date().getFullYear();

    readonly navLinks = Constants.navLinks;
}
