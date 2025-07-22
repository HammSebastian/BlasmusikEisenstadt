import {Component} from '@angular/core';

@Component({
    selector: 'app-constants',
    imports: [],
    template: '',
    styles: ''
})
export class Constants {
    public static readonly navLinks = [
        {label: 'Home', url: '/'},
        {label: 'Events', url: '/events'},
        {label: 'Neuigkeiten', url: '/news'},
        {label: 'Gallerie', url: '/gallery'},
        {label: 'Geschichte', url: '/history'},
        {label: 'Über uns', url: '/about'},
        {label: 'Mitglieder', url: '/members'},
        {label: 'Die Feuerspritzen', url: '/youth'}
    ];
}



