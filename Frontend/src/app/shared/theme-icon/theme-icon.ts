import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-theme-icon',
  imports: [],
  templateUrl: './theme-icon.html',
  styleUrl: './theme-icon.css'
})
export class ThemeIcon {
    @Input() darkMode = false;
}
