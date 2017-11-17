import {Component} from '@angular/core';
import { NavController } from 'ionic-angular';
import {EventPage} from "../event-page/event-page.component";

@Component({
  selector: 'page-dashboard',
  templateUrl: 'dashboard.component.html'
})
export class DashboardComponent {
  constructor(public navCtrl: NavController) {}

  goEventPage() {
    this.navCtrl.push(EventPage)
  }
}
