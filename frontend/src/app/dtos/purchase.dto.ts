import { ProductDTO } from './product.dto';
import { UserBasicDTO } from './user.basic.dto';

export interface PurchaseDTO {
    id: number;
    product: ProductDTO;
    buyer: UserBasicDTO;
    seller: UserBasicDTO;
    purchaseDate: Date;
}
