import { Component } from '@angular/core';
import { AlertController } from 'ionic-angular';


@Component({
  selector: 'event-page',
  templateUrl: 'event-page.component.html'
})
export class EventPage {

constructor(private alertCtrl: AlertController) {}

addSpendingPrompt() {
  let alert = this.alertCtrl.create({
    title: 'Add spending',
    inputs: [
      {
        name: 'title',
        placeholder: 'Title'
      },
      {
        name: 'money',
        placeholder: 'Money'
      },
      {
        name: 'date',
        placeholder: 'Date'
      }
    ],
    buttons: [
      {
        text: 'Cancel',
        role: 'cancel',
        handler: data => {
          console.log('Cancel clicked');
        }
      },
      {
        text: 'Confirm',
        handler: data => {
          this.addSpending(data.title, data.money, data.date)
        }
      }
    ]
  });
  alert.present();
}

addSpending(title: string, money: number, date: string) {

}
}
