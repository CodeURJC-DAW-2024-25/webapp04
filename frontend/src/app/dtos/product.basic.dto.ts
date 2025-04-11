export interface ProductBasicDTO {
    id: number;
    name: string;
    price: number;
    category: string;
    sold: boolean;
    publishDate: string; // ISO 8601 string format for dates
}