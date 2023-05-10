import {Component, Input, OnInit, ViewChild} from '@angular/core';
import { ElementRef, Renderer2 } from '@angular/core';

declare var vis:any;

@Component({
  selector: 'app-vis',
  templateUrl: './vis.component.html',
  styleUrls: ['./vis.component.scss']
})
export class VisComponent implements OnInit {
  @ViewChild("siteConfigNetwork") networkContainer: ElementRef | undefined;
  @ViewChild("svgNetwork") svgNetworkContainer: ElementRef | undefined;

  @Input()
  treeData: any;

  public network: any;

  constructor() { }

  ngOnInit() {

    setTimeout(() => {
      // this.drawSvgNetwork();  // DRAW SVG WITH CUSTOM HTML
      console.log('treeData:', this.treeData)
      this.loadVisTree(this.treeData);
    }, 1000);


 // RENDER STANDARD NODES WITH TEXT LABEL
  }

  loadVisTree(treedata:any) {
    var options = {
      interaction: {
        hover: true,
      },
      manipulation: {
        enabled: false
      },
      physics: {
        enabled: false,
      },
      edges: {
        font: {
          size: 12,
        },
        widthConstraint: {
          maximum: 90,
        },
      },
      nodes: {
        shape: "box",
        margin: 10,
        widthConstraint: {
          maximum: 200,
        },
      },
    };
    var container = this.networkContainer!.nativeElement;
    this.network = new vis.Network(container, treedata, options);

    var that = this;
    this.network.on("hoverNode", function (params: any) {
      console.log('hoverNode Event:', params);
    });
    // this.network.on("blurNode", function(params: any){
    //   console.log('blurNode event:', params);
    // });
  }





  drawSvgNetwork() {
    var nodes = null;
    var edges = null;
    var network = null;

    var DIR = 'img/refresh-cl/';
    var LENGTH_MAIN = 150;
    var LENGTH_SUB = 50;

    var svg = '<svg xmlns="http://www.w3.org/2000/svg" width="390" height="65">' +
      '<rect x="0" y="0" width="100%" height="100%" fill="#7890A7" stroke-width="20" stroke="#ffffff" ></rect>' +
      '<foreignObject x="15" y="10" width="100%" height="100%">' +
      '<div xmlns="http://www.w3.org/1999/xhtml" style="font-family:Arial; font-size:30px">' +
      ' <em>I</em> am' +
      '<span style="color:white; text-shadow:0 0 20px #000000;">' +
      ' HTML in SVG!</span>' +

      // * THIS IMAGE IS NOT RENDERING *
      '<i style="background-image: url(https://openclipart.org/download/280615/July-4th-v2B.svg);"></i>' +

      '</div>' +
      '</foreignObject>' +
      '</svg>';


    var url = "data:image/svg+xml;charset=utf-8,"+ encodeURIComponent(svg);

// Create a data table with nodes.
    nodes = [];

    // Create a data table with links.
    edges = [];

    nodes.push({id: 1, label: 'Get HTML', image: url, shape: 'image'});
    nodes.push({id: 2, label: 'Using SVG', image: url, shape: 'image'});
    edges.push({from: 1, to: 2, length: 300});

    // create a network
    var container = this.svgNetworkContainer!.nativeElement;

    //var container = document.getElementById('mynetwork');
    var data = {
      nodes: nodes,
      edges: edges
    };
    var options = {
      physics: {stabilization: false},
      edges: {smooth: false}
    };
    //network = new vis.Network(container, data, options);
    this.network = new vis.Network(container, data, options);
  }
}
