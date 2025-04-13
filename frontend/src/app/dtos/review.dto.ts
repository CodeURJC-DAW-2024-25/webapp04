import { UserBasicDTO } from "./user.basic.dto";


export interface ReviewDTO {
    id: number;
    rating: number;
    description: string;
    reviewOwner: UserBasicDTO;
    reviewedUser: UserBasicDTO;
}