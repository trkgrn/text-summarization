import {Component, OnInit} from '@angular/core';
import {TextSummarizeRequest} from "./model/request/TextSummarizeRequest";
import {v4 as uuidv4} from 'uuid';
import {MatStepper} from "@angular/material/stepper";
import {DocumentService} from "./services/document.service";
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
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

  isActive: boolean = false;


  constructor(private service: DocumentService) {
  }

  ngOnInit(): void {
  }


  getTreeData() {
    var nodes = [
      {id: 1, label: 'Node 1', title: 'I am node 1!'},
      {id: 2, label: 'Node 2', title: 'I am node 2!'},
      {id: 3, label: 'Node 3'},
      {id: 4, label: 'Node 4'},
      {id: 5, label: 'Node 5'}
    ];

    // create an array with edges
    var edges = [
      {from: 1, to: 2, label: '50'},
      {from: 1, to: 3, label: '55'},
      {from: 1, to: 4,},
      {from: 1, to: 5},
      {from: 2, to: 3},
      {from: 2, to: 4},
      {from: 2, to: 5},
      {from: 3, to: 4},
      {from: 3, to: 5},
      {from: 4, to: 5}
    ];
    var treeData = {
      nodes: nodes,
      edges: edges
    };
    return treeData;
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
    this.connectByUUID(this.request.uuid,stepper);
    await this.service.summarizeText(this.request)
      .then((response) => {
        this.isActive = true;
        console.log(response)
      }).catch((error) => {
        console.log(error);
      });
  }

  connectByUUID(uuid: string,stepper: MatStepper) {
    const destination = '/topic/documents/' + uuid;
    this.socket = new SockJS(this.baseUrl + '/socket');
    let stompClient: Stomp.Client = Stomp.over(this.socket);

    stompClient.connect({}, (frame) => {
      console.log('connected to: ' + frame);
      stompClient!.subscribe(
        destination,
        async (response) => {
          let step = JSON.parse(response.body);
          console.log("STEPP: "+JSON.stringify(step));
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
