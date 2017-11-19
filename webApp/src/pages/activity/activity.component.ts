import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'activity',
  templateUrl: 'activity.component.html'
})
export class Activity {

  constructor(public navCtrl: NavController) {}

  boolExpandOwe : boolean = false;
  boolExpandOwed : boolean = false;

  collapseOrExpandOwe() {
    this.boolExpandOwe = !this.boolExpandOwe;
  }

  collapseOrExpandOwed() {
    this.boolExpandOwed = !this.boolExpandOwed;
  }
}
