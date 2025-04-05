import { UserBasicDTO } from "./user.basic.dto";

export interface ProductDTO {
    id: number;
    name: string;
    price: number;
    category: string;
    description: string;
    owner: UserBasicDTO;
    imageUrls: string[];
    thumbnail: string;
    sold: boolean;
    publishDate: Date;
}
