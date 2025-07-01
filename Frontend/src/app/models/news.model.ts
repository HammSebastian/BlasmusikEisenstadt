export interface NewsArticle {
    id: number;
    title: string;
    summary?: string; // Optional in some contexts
    content: string;
    publicationDate: string; // ISO date string, will be new Date() in component if needed
    imageUrl?: string;
    authorUsername: string;
    authorId: number;
    published: boolean;
    createdAt: string; // ISO date string
    updatedAt: string; // ISO date string
}

// For creating a news article, aligned with CreateNewsArticleRequestDto
export interface CreateNewsArticleRequest {
    title: string;
    summary?: string;
    content: string;
    publicationDate: string; // ISO date string
    imageUrl?: string;
    published?: boolean; // Default to false on backend if not provided
}

// For updating a news article, aligned with UpdateNewsArticleRequestDto
export interface UpdateNewsArticleRequest {
    title: string;
    summary?: string;
    content:string;
    publicationDate: string; // ISO date string
    imageUrl?: string;
    published: boolean; // Usually required for update
}

// For paginated responses
export interface Page<T> {
    content: T[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: {
            sorted: boolean;
            unsorted: boolean;
            empty: boolean;
        };
        offset: number;
        paged: boolean;
        unpaged: boolean;
    };
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number; // Current page number
    sort: {
        sorted: boolean;
        unsorted: boolean;
        empty: boolean;
    };
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}
