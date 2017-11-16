import { Component } from '@angular/core';

import { ContactPage } from '../contact/contact';
import { EventPage } from '../event-page/event-page.component';
import { AddSpending } from '../add-spending/add-spending.component';
import {DashboardComponent} from "../dashboard/dashboard.component";

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = EventPage;
  tab2Root = AddSpending;
  tab3Root = ContactPage;
  tab4Root = DashboardComponent;

  constructor() {

  }
}
