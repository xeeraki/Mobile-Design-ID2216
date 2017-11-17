import { Component } from '@angular/core';

import { ContactPage } from '../contact/contact';
import { AddSpending } from '../add-spending/add-spending.component';
import { DashboardComponent } from "../dashboard/dashboard.component";

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage {

  tab1Root = DashboardComponent;
  tab2Root = AddSpending;
  tab3Root = ContactPage;

  constructor() {

  }
}
