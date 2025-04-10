import { ProductDTO } from "./product.dto";
import { UserBasicDTO } from "./user.basic.dto";
import { MessageDTO } from "./message.dto";

export interface ChatDTO {
  id: number;
  product: ProductDTO;
  userBuyer: UserBasicDTO;
  userSeller: UserBasicDTO;
  messages: MessageDTO[];
  selling: boolean;
}
