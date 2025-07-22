import {NewsEnum} from './enums/news.enum';

export interface NewsModel {
    title: string;
    description: string;
    newsImage: string;
    newsType: NewsEnum;
    date: string;
    published: boolean;
}
