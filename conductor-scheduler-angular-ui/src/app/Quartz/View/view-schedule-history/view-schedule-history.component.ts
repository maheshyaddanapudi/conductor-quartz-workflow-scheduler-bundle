import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ConductorQuartzHistory } from 'src/app/Modals/ConductorQuartzHistory';
import { SchedulerApiCallerService } from 'src/app/Services/scheduler-api-caller.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-view-schedule-history',
  templateUrl: './view-schedule-history.component.html',
  styleUrls: []
})
export class ViewScheduleHistoryComponent implements OnInit {

  conductorQuartzHistoryList: ConductorQuartzHistory[];
  conductorQuartzHistoryFetchError: string;
  scheduleId: string;
  conductorQuartzHistoryLogTemp: string;

  constructor(public router: Router, private modalService: NgbModal, private schedulerApiCallerService: SchedulerApiCallerService, private spinner: NgxSpinnerService) { 

    if(undefined != localStorage.getItem('conductor.scheduleId')){
      this.scheduleId = localStorage.getItem('conductor.scheduleId');
      localStorage.removeItem('conductor.scheduleId');
    }
    else{
      this.router.navigateByUrl('quartz/view/view-all-schedules', { skipLocationChange: true }).then((fulfilled: boolean) => {
        console.log('Routed')
      })
    }

  }

  async ngOnInit() {
    this.spinner.show();

    await this.schedulerApiCallerService.workflowScheduleExecutionHistory(this.scheduleId).then((response: ConductorQuartzHistory[]) => {
      this.conductorQuartzHistoryList = response;
    }).catch(err => {
      this.conductorQuartzHistoryFetchError = JSON.stringify(err);
    });

    this.spinner.hide();

  }

  openLarge(content) {
    this.modalService.open(content, {
      size: 'lg'
    });
  }

  async setconductorQuartzHistoryLogTemp(value: any)
  {
    if(undefined == value || '' == value)
    {
      this.conductorQuartzHistoryLogTemp = undefined
    }
    else{
      this.conductorQuartzHistoryLogTemp = value.quartzExecutionLog;
    }
  }

}
