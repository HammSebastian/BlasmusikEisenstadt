import { Injectable, inject } from '@angular/core';
import { Title, Meta } from '@angular/platform-browser';
import { MetaTags, OpenGraphTags, TwitterCardTags, SeoMetadata } from '../models/seo.model';

/**
 * Service for managing SEO metadata including standard meta tags, Open Graph tags, and Twitter Card tags.
 * This service provides methods to set, update, and clear different types of meta tags.
 */
@Injectable({
  providedIn: 'root'
})
export class SeoService {
  private readonly title = inject(Title);
  private readonly meta = inject(Meta);

  /**
   * Sets the page title
   * @param title - The title to set
   */
  setTitle(title: string): void {
    this.title.setTitle(title);
  }

  /**
   * Gets the current page title
   * @returns The current page title
   */
  getTitle(): string {
    return this.title.getTitle();
  }

  /**
   * Sets standard meta tags
   * @param tags - The meta tags to set
   */
  setMetaTags(tags: MetaTags): void {
    if (tags.title) {
      this.setTitle(tags.title);
    }

    if (tags.description) {
      this.meta.updateTag({ name: 'description', content: tags.description });
    }

    if (tags.keywords) {
      this.meta.updateTag({ name: 'keywords', content: tags.keywords });
    }

    if (tags.author) {
      this.meta.updateTag({ name: 'author', content: tags.author });
    }

    if (tags.robots) {
      this.meta.updateTag({ name: 'robots', content: tags.robots });
    }

    if (tags.canonical) {
      // For canonical URLs, we need to either update or add a link tag
      const canonicalElement = document.querySelector('link[rel="canonical"]');
      if (canonicalElement) {
        (canonicalElement as HTMLLinkElement).href = tags.canonical;
      } else {
        const link = document.createElement('link');
        link.setAttribute('rel', 'canonical');
        link.setAttribute('href', tags.canonical);
        document.head.appendChild(link);
      }
    }
  }

  /**
   * Sets Open Graph meta tags
   * @param tags - The Open Graph tags to set
   */
  setOpenGraphTags(tags: OpenGraphTags): void {
    if (tags.title) {
      this.meta.updateTag({ property: 'og:title', content: tags.title });
    }

    if (tags.description) {
      this.meta.updateTag({ property: 'og:description', content: tags.description });
    }

    if (tags.type) {
      this.meta.updateTag({ property: 'og:type', content: tags.type });
    }

    if (tags.url) {
      this.meta.updateTag({ property: 'og:url', content: tags.url });
    }

    if (tags.image) {
      this.meta.updateTag({ property: 'og:image', content: tags.image });
    }

    if (tags.siteName) {
      this.meta.updateTag({ property: 'og:site_name', content: tags.siteName });
    }

    if (tags.locale) {
      this.meta.updateTag({ property: 'og:locale', content: tags.locale });
    }
  }

  /**
   * Sets Twitter Card meta tags
   * @param tags - The Twitter Card tags to set
   */
  setTwitterCardTags(tags: TwitterCardTags): void {
    if (tags.card) {
      this.meta.updateTag({ name: 'twitter:card', content: tags.card });
    }

    if (tags.site) {
      this.meta.updateTag({ name: 'twitter:site', content: tags.site });
    }

    if (tags.title) {
      this.meta.updateTag({ name: 'twitter:title', content: tags.title });
    }

    if (tags.description) {
      this.meta.updateTag({ name: 'twitter:description', content: tags.description });
    }

    if (tags.image) {
      this.meta.updateTag({ name: 'twitter:image', content: tags.image });
    }

    if (tags.creator) {
      this.meta.updateTag({ name: 'twitter:creator', content: tags.creator });
    }
  }

  /**
   * Sets all SEO metadata including standard meta tags, Open Graph tags, and Twitter Card tags
   * @param metadata - The SEO metadata to set
   */
  setSeoMetadata(metadata: SeoMetadata): void {
    if (metadata.metaTags) {
      this.setMetaTags(metadata.metaTags);
    }

    if (metadata.openGraph) {
      this.setOpenGraphTags(metadata.openGraph);
    }

    if (metadata.twitterCard) {
      this.setTwitterCardTags(metadata.twitterCard);
    }
  }

  /**
   * Clears all meta tags (except for the base tag)
   */
  clearMetaTags(): void {
    // Get all meta tags
    const metaTags = document.querySelectorAll('meta');

    // Remove all meta tags except for charset, viewport, and other essential tags
    metaTags.forEach(tag => {
      const name = tag.getAttribute('name');
      const property = tag.getAttribute('property');

      // Skip essential meta tags
      if (name === 'viewport' || name === 'charset' ||
          tag.hasAttribute('charset') || tag.hasAttribute('http-equiv')) {
        return;
      }

      // Remove all other meta tags
      if (name || property) {
        this.meta.removeTag(name ? `name='${name}'` : `property='${property}'`);
      }
    });

    // Reset title
    this.title.setTitle('');

    // Remove canonical link
    const canonicalElement = document.querySelector('link[rel="canonical"]');
    if (canonicalElement) {
      document.head.removeChild(canonicalElement);
    }
  }

  /**
   * Updates a specific meta tag
   * @param name - The name attribute of the meta tag
   * @param content - The content to set
   */
  updateMetaTag(name: string, content: string): void {
    this.meta.updateTag({ name, content });
  }

  /**
   * Updates a specific Open Graph tag
   * @param property - The property attribute of the meta tag
   * @param content - The content to set
   */
  updateOpenGraphTag(property: string, content: string): void {
    this.meta.updateTag({ property: `og:${property}`, content });
  }

  /**
   * Updates a specific Twitter Card tag
   * @param name - The name attribute of the meta tag
   * @param content - The content to set
   */
  updateTwitterCardTag(name: string, content: string): void {
    this.meta.updateTag({ name: `twitter:${name}`, content });
  }

  /**
   * Removes a specific meta tag
   * @param name - The name attribute of the meta tag to remove
   */
  removeMetaTag(name: string): void {
    this.meta.removeTag(`name='${name}'`);
  }

  /**
   * Removes a specific Open Graph tag
   * @param property - The property attribute of the meta tag to remove
   */
  removeOpenGraphTag(property: string): void {
    this.meta.removeTag(`property='og:${property}'`);
  }

  /**
   * Removes a specific Twitter Card tag
   * @param name - The name attribute of the meta tag to remove
   */
  removeTwitterCardTag(name: string): void {
    this.meta.removeTag(`name='twitter:${name}'`);
  }
}
