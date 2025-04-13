import { ProductDTO } from './product.dto';
import { UserBasicDTO } from './user.basic.dto';

export interface ReportDTO {
    id: number;
    reason: number;
    description: string;
    product: ProductDTO;
    reportCreator: UserBasicDTO;
}