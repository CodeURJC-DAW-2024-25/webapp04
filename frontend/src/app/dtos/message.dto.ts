import { UserBasicDTO } from "./user.basic.dto";

export interface MessageDTO {
  id: number;
  sender: UserBasicDTO;
  message: string;
  sentAt: string;
}
