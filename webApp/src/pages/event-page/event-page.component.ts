import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { AddSpending } from "../add-spending/add-spending.component";

@Component({
  selector: 'event-page',
  templateUrl: 'event-page.component.html'
})
export class EventPage {

constructor(public navCtrl: NavController) {}

  goAddSpending(){
    this.navCtrl.push(AddSpending)
  }
}
