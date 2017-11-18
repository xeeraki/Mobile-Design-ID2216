import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'history',
  templateUrl: 'history.component.html'
})
export class HistoryDetails {

constructor(public navCtrl: NavController) {}

  boolExpand: boolean = false;

  collapseOrExpand() {
    this.boolExpand = !this.boolExpand;
  }

}
