import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {TextSummarizeRequest} from "../model/request/TextSummarizeRequest";

@Injectable({
  providedIn: 'root'
})
export class DocumentService {
  baseUrl = 'http://localhost:8080/api/v1/document';

  constructor(private http: HttpClient) {
  }

  public summarizeText(body: TextSummarizeRequest) {
    return this.http.post(this.baseUrl + '/text-summarize', body).toPromise();
  }

}
