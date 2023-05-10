import {DocumentResponse} from "./DocumentResponse";

export class TextSummarizeResponse {
  summary!: string;
  reference!: string;
  rougeScore!: string;
  document!: DocumentResponse;
}
