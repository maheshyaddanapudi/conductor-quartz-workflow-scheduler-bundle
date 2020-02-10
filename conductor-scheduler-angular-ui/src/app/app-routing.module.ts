import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {BaseLayoutComponent} from './Layout/base-layout/base-layout.component';
import {PagesLayoutComponent} from './Layout/pages-layout/pages-layout.component';
import { ViewAllWorkflowsComponent } from './Conductor/View/view-all-workflows/view-all-workflows.component';
import { ScheduleWorlfkowComponent } from './Conductor/Schedule/schedule-worlfkow/schedule-worlfkow.component';
import { SchedulerPortfolioDashboardComponent } from './Dashboards/Scheduler/scheduler-portfolio-dashboard/scheduler-portfolio-dashboard.component';
import { ViewAllSchedulesComponent } from './Quartz/View/view-all-schedules/view-all-schedules.component';
import { ViewScheduleHistoryComponent } from './Quartz/View/view-schedule-history/view-schedule-history.component';

const routes: Routes = [
  {
    path: '',
    component: BaseLayoutComponent,
    children: [

      // Dashboads

      {path: '', component: SchedulerPortfolioDashboardComponent, data: {extraParameter: 'dashboardsMenu'}},

      // Conductor

      {path: 'conductor/view/view-all-workflows', component: ViewAllWorkflowsComponent, data: {extraParameter: 'conductorMenu'}},
      {path: 'conductor/schedule/schedule-workflow', component: ScheduleWorlfkowComponent, data: {extraParameter: 'conductorMenu'}},

      // Quartz

      {path: 'quartz/view/view-all-schedules', component: ViewAllSchedulesComponent, data: {extraParameter: 'quartzMenu'}},
      {path: 'quartz/view/view-schedule-history', component: ViewScheduleHistoryComponent, data: {extraParameter: 'quartzMenu'}},

      // Dashboards

      {path: 'dashboards/scheduler/scheduler-portfolio-dashboard', component: SchedulerPortfolioDashboardComponent, data: {extraParameter: 'dashboardMenu'}},

    ]

  },
  {path: '**', redirectTo: ''}
];

@NgModule({
  imports: [RouterModule.forRoot(routes,
    {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
    })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
