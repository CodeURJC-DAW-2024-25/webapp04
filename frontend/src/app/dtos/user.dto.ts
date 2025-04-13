import { ProductDTO } from "./product.dto";
import { ReviewDTO } from "./review.dto";
import { PurchaseDTO } from "./purchase.dto";

export interface UserDTO {
    id: number;
    name: string;
    creationYear: number;
    roles: string[];
    products: ProductDTO[];
    reviews: ReviewDTO[];
    sales: PurchaseDTO[];
    purchases: PurchaseDTO[];
    iframe: string;
    image: string;
    hasImage: boolean;
}
