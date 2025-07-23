import {ImageModel} from './image.model';

export interface GalleryModel {
    title: string;
    fromDate: string;
    images: ImageModel[];
}
