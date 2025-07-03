import { Injectable, inject } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';

export interface MetaTagConfig {
    title: string;
    description: string;
    keywords?: string;
    ogTitle?: string;
    ogDescription?: string;
    ogImage?: string;
    ogUrl?: string;
    twitterCard?: string;
    twitterTitle?: string;
    twitterDescription?: string;
    twitterImage?: string;
}

@Injectable({
    providedIn: 'root'
})
export class SeoService {
    private meta = inject(Meta);
    private title = inject(Title);

    updateMetaTags(config: MetaTagConfig): void {
        // Update title
        this.title.setTitle(config.title);

        // Basic meta tags
        this.meta.updateTag({ name: 'description', content: config.description });
        if (config.keywords) {
            this.meta.updateTag({ name: 'keywords', content: config.keywords });
        }

        // Open Graph tags
        this.meta.updateTag({ property: 'og:title', content: config.ogTitle || config.title });
        this.meta.updateTag({ property: 'og:description', content: config.ogDescription || config.description });
        this.meta.updateTag({ property: 'og:type', content: 'website' });

        if (config.ogImage) {
            this.meta.updateTag({ property: 'og:image', content: config.ogImage });
        }
        if (config.ogUrl) {
            this.meta.updateTag({ property: 'og:url', content: config.ogUrl });
        }

        // Twitter Card tags
        this.meta.updateTag({ name: 'twitter:card', content: config.twitterCard || 'summary_large_image' });
        this.meta.updateTag({ name: 'twitter:title', content: config.twitterTitle || config.title });
        this.meta.updateTag({ name: 'twitter:description', content: config.twitterDescription || config.description });

        if (config.twitterImage || config.ogImage) {
            this.meta.updateTag({ name: 'twitter:image', content: config.twitterImage || config.ogImage! });
        }
    }

    updateCanonicalUrl(url: string): void {
        const head = document.getElementsByTagName('head')[0];
        let element: HTMLLinkElement = document.querySelector("link[rel='canonical']") as HTMLLinkElement;

        if (!element) {
            element = document.createElement('link') as HTMLLinkElement;
            element.setAttribute('rel', 'canonical');
            head.appendChild(element);
        }

        element.setAttribute('href', url);
    }
}
