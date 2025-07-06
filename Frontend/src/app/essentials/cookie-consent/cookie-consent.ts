import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterLink} from '@angular/router';


@Component({
    selector: 'app-cookie-consent',
    imports: [CommonModule, RouterLink],
    templateUrl: './cookie-consent.html',
    styleUrl: './cookie-consent.css'
})
export class CookieConsent implements OnInit {
    // Signal zur Steuerung der Sichtbarkeit des Cookie-Banners
    showConsent = signal(false);
    // Schlüssel für die Speicherung des Consent-Status im localStorage
    private readonly CONSENT_KEY = 'blasmusik_cookie_consent';

    ngOnInit(): void {
        // Prüft beim Initialisieren der Komponente, ob der Cookie-Consent bereits gegeben wurde
        // Die Prüfung 'typeof localStorage !== 'undefined'' ist wichtig für SSR-Kompatibilität
        if (typeof localStorage !== 'undefined') {
            const consent = localStorage.getItem(this.CONSENT_KEY);
            if (!consent) {
                // Zeigt den Cookie-Banner nach einer kurzen Verzögerung an, falls noch kein Consent vorliegt
                // Das Delay kann angepasst oder entfernt werden, je nach UX-Anforderungen und rechtlichen Vorgaben
                setTimeout(() => {
                    this.showConsent.set(true);
                }, 1000); // 1 Sekunde Verzögerung
            }
        }
    }

    /**
     * Akzeptiert alle Cookies und versteckt den Banner.
     */
    protected acceptAll(): void {
        this.setConsent('all');
        this.showConsent.set(false);
    }

    /**
     * Akzeptiert nur essenzielle Cookies und versteckt den Banner.
     */
    protected acceptEssential(): void {
        this.setConsent('essential');
        this.showConsent.set(false);
    }

    /**
     * Speichert den Consent-Status im localStorage.
     * @param type Der Typ des akzeptierten Consents ('all' oder 'essential').
     */
    private setConsent(type: 'all' | 'essential'): void {
        if (typeof localStorage !== 'undefined') {
            const consent = {
                type,
                timestamp: new Date().toISOString(), // Speichert den Zeitpunkt des Consents
                version: '1.0' // Version des Consent-Formats für zukünftige Änderungen
            };
            localStorage.setItem(this.CONSENT_KEY, JSON.stringify(consent));
        }
    }
}

