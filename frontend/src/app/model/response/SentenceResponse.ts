import {SimilarityResponse} from "./SimilarityResponse";

export class SentenceResponse{
  includeOnId!: number;
  sentenceNo!: number;
  sentenceId!: number;
  text!: string;
  numberOfEdgeExceedingThreshold!: number;
  sentenceScore!: number;
  isIncludedSummary!: boolean;
  similarities!: SimilarityResponse[];

}
