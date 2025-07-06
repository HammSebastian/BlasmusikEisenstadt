import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cookie-consent',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cookie-consent.component.html',
  styleUrl: './cookie-consent.component.css'
})
export class CookieConsentComponent implements OnInit {
  showConsent = signal(false);
  private readonly CONSENT_KEY = 'blasmusik_cookie_consent';

  ngOnInit(): void {
    // Only show consent if not already given and we're in browser
    if (typeof localStorage !== 'undefined') {
      const consent = localStorage.getItem(this.CONSENT_KEY);
      if (!consent) {
        // Show consent popup after a short delay
        setTimeout(() => {
          this.showConsent.set(true);
        }, 1000);
      }
    }
  }

  protected acceptAll(): void {
    this.setConsent('all');
    this.showConsent.set(false);
  }

  protected acceptEssential(): void {
    this.setConsent('essential');
    this.showConsent.set(false);
  }

  private setConsent(type: 'all' | 'essential'): void {
    if (typeof localStorage !== 'undefined') {
      const consent = {
        type,
        timestamp: new Date().toISOString(),
        version: '1.0'
      };
      localStorage.setItem(this.CONSENT_KEY, JSON.stringify(consent));
    }
  }
}