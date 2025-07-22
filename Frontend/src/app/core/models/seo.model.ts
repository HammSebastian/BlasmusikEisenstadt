/**
 * Interface for standard meta tags
 */
export interface MetaTags {
    title?: string;
    description?: string;
    keywords?: string;
    author?: string;
    robots?: string;
    canonical?: string;
}

/**
 * Interface for Open Graph meta tags
 */
export interface OpenGraphTags {
    title?: string;
    description?: string;
    type?: string;
    url?: string;
    image?: string;
    siteName?: string;
    locale?: string;
}

/**
 * Interface for Twitter Card meta tags
 */
export interface TwitterCardTags {
    card?: string;
    site?: string;
    title?: string;
    description?: string;
    image?: string;
    creator?: string;
}

/**
 * Combined interface for all SEO metadata
 */
export interface SeoMetadata {
    metaTags?: MetaTags;
    openGraph?: OpenGraphTags;
    twitterCard?: TwitterCardTags;
}
