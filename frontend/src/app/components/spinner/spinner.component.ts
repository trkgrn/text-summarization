import {Component, Input, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {

  @Input() text: string = "Loading..."

  constructor(private spinner: NgxSpinnerService) {
    this.spinner.show();
  }

  ngOnInit(): void {
  }

}
