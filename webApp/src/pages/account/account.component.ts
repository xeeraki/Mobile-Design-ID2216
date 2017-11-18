import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

import { AccountDetails } from "../account-details/account-details.component";
import { PaymentInfo } from "../payment-info/payment-info.component";
import { HistoryDetails } from "../history/history.component";
import { Feedback } from "../feedback/feedback.component";


@Component({
  selector: 'account',
  templateUrl: 'account.component.html'
})
export class Account {

  constructor(public navCtrl: NavController) {}

  goAccountDetails() {
    this.navCtrl.push(AccountDetails)
  }

  goPaymentInfo() {
    this.navCtrl.push(PaymentInfo)
  }

  goHistory() {
    this.navCtrl.push(HistoryDetails)
  }

  goFeedback() {
    this.navCtrl.push(Feedback)
  }
}
