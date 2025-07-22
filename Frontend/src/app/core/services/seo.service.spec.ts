import { TestBed } from '@angular/core/testing';
import { Title, Meta } from '@angular/platform-browser';
import { SeoService } from './seo.service';
import { SeoMetadata } from '../models/seo.model';

describe('SeoService', () => {
  let service: SeoService;
  let titleService: jasmine.SpyObj<Title>;
  let metaService: jasmine.SpyObj<Meta>;

  beforeEach(() => {
    const titleSpy = jasmine.createSpyObj('Title', ['setTitle', 'getTitle']);
    const metaSpy = jasmine.createSpyObj('Meta', ['updateTag', 'removeTag']);

    TestBed.configureTestingModule({
      providers: [
        SeoService,
        { provide: Title, useValue: titleSpy },
        { provide: Meta, useValue: metaSpy }
      ]
    });

    service = TestBed.inject(SeoService);
    titleService = TestBed.inject(Title) as jasmine.SpyObj<Title>;
    metaService = TestBed.inject(Meta) as jasmine.SpyObj<Meta>;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set title', () => {
    const testTitle = 'Test Title';
    service.setTitle(testTitle);
    expect(titleService.setTitle).toHaveBeenCalledWith(testTitle);
  });

  it('should get title', () => {
    const testTitle = 'Test Title';
    titleService.getTitle.and.returnValue(testTitle);
    expect(service.getTitle()).toBe(testTitle);
  });

  it('should set meta tags', () => {
    const metaTags = {
      title: 'Test Title',
      description: 'Test Description',
      keywords: 'test, keywords',
      author: 'Test Author',
      robots: 'index, follow',
      canonical: 'https://example.com'
    };

    // Mock document.querySelector and document.createElement
    spyOn(document, 'querySelector').and.returnValue(null);
    const mockLink = document.createElement('link');
    spyOn(document, 'createElement').and.returnValue(mockLink);
    const mockHead = document.createElement('head');
    spyOn(document, 'head').and.returnValue(mockHead);
    spyOn(mockHead, 'appendChild');

    service.setMetaTags(metaTags);

    expect(titleService.setTitle).toHaveBeenCalledWith(metaTags.title);
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'description', content: metaTags.description });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'keywords', content: metaTags.keywords });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'author', content: metaTags.author });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'robots', content: metaTags.robots });
    expect(document.createElement).toHaveBeenCalledWith('link');
    expect(mockHead.appendChild).toHaveBeenCalledWith(mockLink);
  });

  it('should set Open Graph tags', () => {
    const ogTags = {
      title: 'OG Title',
      description: 'OG Description',
      type: 'website',
      url: 'https://example.com',
      image: 'https://example.com/image.jpg',
      siteName: 'Example Site',
      locale: 'en_US'
    };

    service.setOpenGraphTags(ogTags);

    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:title', content: ogTags.title });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:description', content: ogTags.description });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:type', content: ogTags.type });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:url', content: ogTags.url });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:image', content: ogTags.image });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:site_name', content: ogTags.siteName });
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:locale', content: ogTags.locale });
  });

  it('should set Twitter Card tags', () => {
    const twitterTags = {
      card: 'summary_large_image',
      site: '@example',
      title: 'Twitter Title',
      description: 'Twitter Description',
      image: 'https://example.com/image.jpg',
      creator: '@creator'
    };

    service.setTwitterCardTags(twitterTags);

    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:card', content: twitterTags.card });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:site', content: twitterTags.site });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:title', content: twitterTags.title });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:description', content: twitterTags.description });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:image', content: twitterTags.image });
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:creator', content: twitterTags.creator });
  });

  it('should set all SEO metadata', () => {
    const seoMetadata: SeoMetadata = {
      metaTags: {
        title: 'Test Title',
        description: 'Test Description'
      },
      openGraph: {
        title: 'OG Title',
        description: 'OG Description'
      },
      twitterCard: {
        title: 'Twitter Title',
        description: 'Twitter Description'
      }
    };

    // Spy on the individual methods
    spyOn(service, 'setMetaTags');
    spyOn(service, 'setOpenGraphTags');
    spyOn(service, 'setTwitterCardTags');

    service.setSeoMetadata(seoMetadata);

    expect(service.setMetaTags).toHaveBeenCalledWith(seoMetadata.metaTags);
    expect(service.setOpenGraphTags).toHaveBeenCalledWith(seoMetadata.openGraph);
    expect(service.setTwitterCardTags).toHaveBeenCalledWith(seoMetadata.twitterCard);
  });

  it('should update and remove specific meta tags', () => {
    service.updateMetaTag('description', 'Updated description');
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'description', content: 'Updated description' });

    service.updateOpenGraphTag('title', 'Updated OG title');
    expect(metaService.updateTag).toHaveBeenCalledWith({ property: 'og:title', content: 'Updated OG title' });

    service.updateTwitterCardTag('title', 'Updated Twitter title');
    expect(metaService.updateTag).toHaveBeenCalledWith({ name: 'twitter:title', content: 'Updated Twitter title' });

    service.removeMetaTag('description');
    expect(metaService.removeTag).toHaveBeenCalledWith("name='description'");

    service.removeOpenGraphTag('title');
    expect(metaService.removeTag).toHaveBeenCalledWith("property='og:title'");

    service.removeTwitterCardTag('title');
    expect(metaService.removeTag).toHaveBeenCalledWith("name='twitter:title'");
  });
});
