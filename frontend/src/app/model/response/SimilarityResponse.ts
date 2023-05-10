import {TargetSentenceResponse} from "./TargetSentenceResponse";

export class SimilarityResponse{
  similarityId!: number;
  similarityRate!: number;
  sentence!: TargetSentenceResponse;
}
