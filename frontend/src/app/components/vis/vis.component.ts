import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ElementRef, Renderer2} from '@angular/core';

declare var vis: any;

@Component({
  selector: 'app-vis',
  templateUrl: './vis.component.html',
  styleUrls: ['./vis.component.scss']
})
export class VisComponent implements OnInit {
  @ViewChild("siteConfigNetwork") networkContainer: ElementRef | undefined;

  @Input()
  treeData: any;

  public network: any;

  constructor() {
  }

  ngOnInit() {

    setTimeout(() => {
      // this.drawSvgNetwork();  // DRAW SVG WITH CUSTOM HTML
      console.log('treeData:', this.treeData)
      this.loadVisTree(this.treeData);
    }, 1000);


    // RENDER STANDARD NODES WITH TEXT LABEL
  }

  loadVisTree(treedata: any) {

    var options = {
      interaction: {
        hover: true,
        hoverConnectedEdges: true,
      },
      manipulation: {
        enabled: false
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
      autoResize: true
    };

    var edges = new vis.DataSet(treedata.edges);
    var nodes = new vis.DataSet(treedata.nodes);
    var data = {nodes: nodes, edges: edges};
    var container = this.networkContainer!.nativeElement;
    const network = new vis.Network(container, data, options);


    network.on("selectNode", function (params: any) {
      var nodeId = params.nodes[0];
      var connectedEdges = network.getConnectedEdges(nodeId);
      var allEdges = edges.get();

      for (var i = 0; i < allEdges.length; i++) {
        var edge = allEdges[i];
        if (connectedEdges.indexOf(edge.id) !== -1 || edge.label =='' ) {
          edges.update({id: edge.id, hidden: false});
        } else {
          edges.update({id: edge.id, hidden: true});
        }
      }
    });

    network.on("deselectNode", function (params: any) {
      var allEdges = edges.get();
      for (var i = 0; i < allEdges.length; i++) {
        edges.update({id: allEdges[i].id, hidden: false});
      }
    });

  }

}
