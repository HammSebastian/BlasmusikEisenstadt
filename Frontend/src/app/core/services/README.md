# Angular SEO Service

This directory contains a reusable Angular SEO service that helps manage meta data dynamically for each page or component in the application.

## SeoService

The `SeoService` is a comprehensive service for managing SEO metadata, including standard meta tags, Open Graph tags, and Twitter Card tags. It provides methods to set, update, and clear different types of meta tags.

### Features

- Uses Angular's Title and Meta services to set page titles and meta tags dynamically
- Supports setting standard meta tags like description, keywords, author, robots
- Supports Open Graph (og:) meta tags for social media sharing
- Supports Twitter Card meta tags for Twitter sharing
- Provides methods to update and clear meta tags
- Easy to integrate into any component via dependency injection
- Follows Angular best practices and is typesafe

### Usage

Here's an example of how to use the SEO service in a component:

```typescript
import { Component, OnInit, inject } from '@angular/core';
import { SeoService } from 'path/to/seo.service';

@Component({
  selector: 'app-example',
  templateUrl: './example.html',
  styleUrl: './example.css'
})
export class ExampleComponent implements OnInit {
  private readonly seoService = inject(SeoService);

  ngOnInit() {
    this.setSeoMetadata();
  }

  private setSeoMetadata(): void {
    this.seoService.setSeoMetadata({
      metaTags: {
        title: 'Page Title',
        description: 'Page description for search engines',
        keywords: 'keyword1, keyword2, keyword3',
        author: 'Author Name',
        robots: 'index, follow',
        canonical: 'https://example.com/page'
      },
      openGraph: {
        title: 'Open Graph Title',
        description: 'Description for social media sharing',
        type: 'website',
        url: 'https://example.com/page',
        image: 'https://example.com/image.jpg',
        siteName: 'Site Name',
        locale: 'en_US'
      },
      twitterCard: {
        card: 'summary_large_image',
        site: '@twitterHandle',
        title: 'Twitter Card Title',
        description: 'Description for Twitter sharing',
        image: 'https://example.com/image.jpg',
        creator: '@creatorHandle'
      }
    });
  }
}
```

## Additional SEO Considerations

### Angular Universal (Server-Side Rendering)

For optimal SEO in Angular applications, consider implementing Angular Universal for server-side rendering (SSR). Angular Universal pre-renders your application on the server, which:

1. Improves performance by showing the first page quickly
2. Facilitates web crawlers to better index your site (SEO)
3. Looks better on social media when users share your links

To implement Angular Universal:

```bash
ng add @nguniversal/express-engine
```

Learn more about Angular Universal at [https://angular.dev/guide/ssr](https://angular.dev/guide/ssr).

### sitemap.xml

A sitemap.xml file helps search engines discover and index your website's pages. It's a structured XML file that lists all the URLs on your website along with metadata about each URL.

Example sitemap.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>https://example.com/</loc>
    <lastmod>2025-07-22</lastmod>
    <changefreq>weekly</changefreq>
    <priority>1.0</priority>
  </url>
  <url>
    <loc>https://example.com/about</loc>
    <lastmod>2025-07-20</lastmod>
    <changefreq>monthly</changefreq>
    <priority>0.8</priority>
  </url>
  <!-- Add more URLs here -->
</urlset>
```

Place your sitemap.xml file in the assets directory and make sure it's copied to the dist folder during build.

### robots.txt

A robots.txt file tells search engine crawlers which pages or files they can or can't request from your site. It's a simple text file placed at the root of your website.

Example robots.txt:

```
User-agent: *
Allow: /
Disallow: /admin/
Disallow: /private/

Sitemap: https://example.com/sitemap.xml
```

Place your robots.txt file in the assets directory and make sure it's copied to the dist folder during build.

## Best Practices

1. **Use unique titles and descriptions** for each page
2. **Keep titles under 60 characters** and descriptions under 160 characters
3. **Include relevant keywords** in titles and descriptions
4. **Use canonical URLs** to avoid duplicate content issues
5. **Implement structured data** (JSON-LD) for rich search results
6. **Optimize images** with descriptive filenames and alt text
7. **Create a logical URL structure** that's easy to understand
8. **Ensure your site is mobile-friendly** and loads quickly
9. **Use HTTPS** for secure connections
10. **Monitor your SEO performance** using tools like Google Search Console
