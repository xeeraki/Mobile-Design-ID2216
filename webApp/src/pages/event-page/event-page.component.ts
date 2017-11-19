import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import { AddSpending } from "../add-spending/add-spending.component";
import { Checkout } from "../checkout/checkout.component";


@Component({
  selector: 'event-page',
  templateUrl: 'event-page.component.html'
})
export class EventPage {

constructor(public navCtrl: NavController) {}

  goAddSpending(){
    this.navCtrl.push(AddSpending)
  }

  goCheckOut(){
    this.navCtrl.push(Checkout)
  }
}
