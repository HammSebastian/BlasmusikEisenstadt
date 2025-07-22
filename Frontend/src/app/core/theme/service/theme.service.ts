import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class ThemeService {
    private isDarkTheme = false;

    constructor() {
        this.isDarkTheme = window.matchMedia('(prefers-color-scheme: dark)').matches;
        this.applyTheme();

        window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
            this.isDarkTheme = e.matches;
            this.applyTheme();
        });
    }

    toggleTheme() {
        this.isDarkTheme = !this.isDarkTheme;
        this.applyTheme();
        localStorage.setItem('theme', this.isDarkTheme ? 'dark' : 'light');
    }

    applyTheme() {
        const theme = this.isDarkTheme ? 'dark-theme' : 'light-theme';
        document.body.classList.remove('light-theme', 'dark-theme');
        document.body.classList.add(theme);
    }

    isDarkMode(): boolean {
        return this.isDarkTheme;
    }

    initializeTheme() {
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme) {
            this.isDarkTheme = savedTheme === 'dark';
            this.applyTheme();
        }
    }
}
