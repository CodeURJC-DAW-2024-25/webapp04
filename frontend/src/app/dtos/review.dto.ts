import { UserBasicDTO } from './user.basic.dto';

export interface ReviewDTO {
    id: number;
    description: string;
    reviewOwner: UserBasicDTO;
    reviewedUser: UserBasicDTO;
    rating: number;
}