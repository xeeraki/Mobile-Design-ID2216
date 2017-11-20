import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';

import { EventPage } from '../pages/event-page/event-page.component';
import { AddSpending } from '../pages/add-spending/add-spending.component';
import { AddEvent } from '../pages/add-event/add-event.component';
import { Activity } from '../pages/activity/activity.component';
import { Account } from '../pages/account/account.component';
import { Checkout } from '../pages/checkout/checkout.component';

import { AccountDetails } from '../pages/account-details/account-details.component';
import { HistoryDetails } from '../pages/history/history.component';
import { PaymentInfo } from '../pages/payment-info/payment-info.component';
import { Feedback } from '../pages/feedback/feedback.component';


import {DashboardComponent} from "../pages/dashboard/dashboard.component";

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

@NgModule({
  declarations: [
    MyApp,
    EventPage,
    DashboardComponent,
    AddSpending,
    AddEvent,
    Activity,
    Account,
    AccountDetails,
    PaymentInfo,
    HistoryDetails,
    Feedback,
    Checkout
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    EventPage,
    DashboardComponent,
    AddSpending,
    AddEvent,
    Activity,
    Account,
    AccountDetails,
    PaymentInfo,
    HistoryDetails,
    Feedback,
    Checkout
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler}
  ]
})
export class AppModule {}
