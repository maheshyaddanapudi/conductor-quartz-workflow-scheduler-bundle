import { Component, OnInit } from '@angular/core';
import { SchedulerApiCallerService } from 'src/app/Services/scheduler-api-caller.service';
import { SchedulePortfolioStats } from 'src/app/Modals/SchedulePortfolioStats';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-scheduler-portfolio-dashboard',
  templateUrl: './scheduler-portfolio-dashboard.component.html',
  styleUrls: []
})
export class SchedulerPortfolioDashboardComponent implements OnInit {

  total: number;
  failed: number;
  success:number;
  percentage: number;
  schedulePortfolioStatsError: string;

  constructor(private schedulerApiCallerService: SchedulerApiCallerService, private sprinner: NgxSpinnerService) { 
    this.sprinner.show();
    this.schedulerApiCallerService.schedulerPortfolioStats().then((response: SchedulePortfolioStats) => {
      this.total = response.total;
      this.failed = response.failed;
      this.success = response.success;
      this.percentage = response.percentage;
    }).catch(err => {
      this.schedulePortfolioStatsError = JSON.stringify(err);
    });
    this.sprinner.hide();
  }

  async ngOnInit() {
    
  }

}
