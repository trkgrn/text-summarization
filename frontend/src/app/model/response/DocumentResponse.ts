import {SentenceResponse} from "./SentenceResponse";

export class DocumentResponse{
  documentId!: number;
  name!: string;
  title!: string;
  sentences!: SentenceResponse[];
}
