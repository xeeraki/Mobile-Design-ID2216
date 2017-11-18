import {Component} from '@angular/core';
import { NavController } from 'ionic-angular';
import { EventPage } from "../event-page/event-page.component";
import { AddEvent } from "../add-event/add-event.component";
import { Activity } from "../activity/activity.component";
import { Account } from "../account/account.component";


@Component({
  selector: 'page-dashboard',
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent {
  constructor(public navCtrl: NavController) {}

  goEventPage() {
    this.navCtrl.push(EventPage)
  }

  goAddEventPage() {
    this.navCtrl.push(AddEvent)
  }

  goActivity() {
    this.navCtrl.push(Activity)
  }

  goAccount() {
    this.navCtrl.push(Account)
  }
}
