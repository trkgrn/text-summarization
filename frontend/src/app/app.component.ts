import {Component, OnInit} from '@angular/core';
import {TextSummarizeRequest} from "./model/request/TextSummarizeRequest";
import {v4 as uuidv4} from 'uuid';
import {MatStepper} from "@angular/material/stepper";
import {DocumentService} from "./services/document.service";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import {TextSummarizeResponse} from "./model/response/TextSummarizeResponse";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'text-summarization-frontend';
  baseUrl = 'http://localhost:8080';
  socket?: WebSocket;
  stompClient = Stomp.Client;
  request: TextSummarizeRequest = new TextSummarizeRequest();
  response!: TextSummarizeResponse;

  isActive: boolean = false;


  constructor(private service: DocumentService) {
  }

  ngOnInit(): void {
  }


  getTreeData() {
    var nodes: {
      id: number; label: string; title: string; x: number; y: number;
      shape: string; physics: boolean; color: any
    }[] = []
    var edges: { from: number; to: number; label: string; labelHighlightBold: boolean; color: any }[] = []

    var sentences = this.response.document.sentences;

    var nodeCount = sentences.length;
    var radius = nodeCount * 40;


    sentences.map((sentence, index) => {
      var angle = index * 2 * Math.PI / nodeCount;
      var x = radius * Math.cos(angle) + 300;
      var y = radius * Math.sin(angle) + 300;
      var id = sentence.sentenceId;
      nodes.push({
        id: id, label: sentence.text, title: sentence.sentenceNo.toString(), x: x, y: y, shape: 'box', physics: false,
        color: {background: '#97C2FC', border: '#2B7CE9', highlight: {background: '#D2E5FF', border: '#2B7CE9'}}
      })
      nodes.push({
        id: id + 1000, label: sentence.sentenceScore.toString(),
        title: sentence.sentenceScore.toString(), x: 0, y: 0, shape: 'circle', physics: true,
        color: {
          background: '#ef6767', border: '#ef6767', highlight: {background: '#ef6767', border: '#ef6767'},
          hover: {background: '#ef6767', border: '#ef6767'}
        }
      });
      nodes.push({
        id: id + 2000, label: sentence.numberOfEdgeExceedingThreshold.toString(),
        title: sentence.numberOfEdgeExceedingThreshold.toString(), x: 0, y: 0, shape: 'circle', physics: true,
        color: {
          background: '#f4cf73', border: '#f4cf73', highlight: {background: '#fdc452', border: '#fdc452'},
          hover: {background: '#fdc452', border: '#fdc452'}
        }
      });
      edges.push({from: id, to: id + 1000, label: '', labelHighlightBold: false, color: '#97C2FC'});
      edges.push({from: id, to: id + 2000, label: '', labelHighlightBold: false, color: '#97C2FC'});

    });


    sentences.map((sentence, index) => {
      sentence.similarities.map((similarity) => {
        const isIncluded = similarity.similarityRate > this.request.similarityThreshold;
        edges.push({
          from: sentence.sentenceId,
          to: similarity.sentence.sentenceId,
          label: similarity.similarityRate.toString(),
          labelHighlightBold: isIncluded,
          color: {
            color: isIncluded ? '#ef6767' : '#97C2FC',
            highlight: isIncluded ? '#ef6767' : '#97C2FC',
            hover: isIncluded ? '#ef6767' : '#97C2FC'
          }
        })
      })
    });

    return {
      nodes: nodes,
      edges: edges
    };
  }


  readDocument(event: any) {
    const file: File = event.target.files[0];

    if (file == undefined) {
      this.request.title = "";
      this.request.text = "";
      this.request.uuid = "";
      return;
    }
    const fileReader = new FileReader();

    fileReader.onload = (e) => {
      const fileContent: string = fileReader.result!.toString();
      const {title, text} = this.extractTitleAndText(fileContent);
      console.log(title);
      console.log(text);
      this.request.title = title;
      this.request.text = text;
      this.request.uuid = uuidv4();
    };
    fileReader.readAsText(file);
  }

  extractTitleAndText(fileContent: string) {
    const lines = fileContent.split('\n');
    const title = lines[0].trim(); // ilk satır başlık
    const text = lines.slice(1).join('\n').trim(); // diğer satırlar metin
    return {title, text};
  }

  isValidRequest(): boolean {
    return this.request.text != undefined && this.request.uuid != undefined && this.request.title != undefined
      && this.request.referenceSummary != undefined && this.request.similarityThreshold != undefined && this.request.scoreThreshold != undefined
      && this.request.referenceSummary != "" && this.request.text != "" && this.request.title != "" && this.request.uuid != "";
  }

  async summarize(stepper: MatStepper) {

    this.connectByUUID(this.request.uuid, stepper);
    await this.service.summarizeText(this.request)
      .then((response: any) => {
        this.isActive = true;
        console.log(response)
        this.response = response;
      }).catch((error) => {
        console.log(error);
      });
  }

  connectByUUID(uuid: string, stepper: MatStepper) {
    const destination = '/topic/documents/' + uuid;
    this.socket = new SockJS(this.baseUrl + '/socket');
    let stompClient: Stomp.Client = Stomp.over(this.socket);

    stompClient.connect({}, (frame) => {
      console.log('connected to: ' + frame);
      stompClient!.subscribe(
        destination,
        async (response) => {
          let step = JSON.parse(response.body);
          console.log("STEPP: " + JSON.stringify(step));
          stepper.next();
        }
      );
    });
  }


  reset(stepper: MatStepper) {
    this.request = new TextSummarizeRequest();
    stepper.reset();
  }


}
