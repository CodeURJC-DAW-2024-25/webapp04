import { ProductBasicDTO } from './product.basic.dto';
import { PurchaseDTO } from './purchase.dto';
import { ReviewDTO } from './review.dto';

export interface UserDTO {
    id: number;
    name: string;
    creationYear: number;
    image: string;
    hasImage: boolean;
    roles: string[];
    products: ProductBasicDTO[];
    reviews: ReviewDTO[];
    sales: PurchaseDTO[];
    purchases: PurchaseDTO[];
    iframe: string;
}