import { ProductBasicDTO } from './product.basic.dto';
import { UserBasicDTO } from './user.basic.dto';

export interface PurchaseDTO {
    id: number;
    product: ProductBasicDTO;
    buyer: UserBasicDTO;
    seller: UserBasicDTO;
    purchaseDate: string; // ISO 8601 format (e.g., "2023-03-15")
}
