import { Component, OnInit } from '@angular/core';
import { SchedulerApiCallerService } from 'src/app/Services/scheduler-api-caller.service';
import { ConductorQuartzMapping } from 'src/app/Modals/ConductorQuartzMapping';
import { NgxSpinnerService } from 'ngx-spinner';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { SchedulerResponse } from 'src/app/Modals/SchedulerResponse';

@Component({
  selector: 'app-view-all-schedules',
  templateUrl: './view-all-schedules.component.html',
  styleUrls: []
})
export class ViewAllSchedulesComponent implements OnInit {

  conductorQuartzMappingList: ConductorQuartzMapping[];
  conductorQuartzMappingError: string;
  conductorQuartzMappingActionSuccess: string;
  conductorQuartzMappingActionError: string;

  conductorQuartzMappingJsonTemp: string;

  constructor(private schedulerApiCallerService: SchedulerApiCallerService, private sprinner: NgxSpinnerService, private modalService: NgbModal, public router: Router) { }

  async ngOnInit() {

    this.sprinner.show();
    await this.schedulerApiCallerService.allWorkflowExecutionSchedules().then((response: ConductorQuartzMapping[]) => {
      this.conductorQuartzMappingList = response;
      this.conductorQuartzMappingError = JSON.stringify(response);
    }).catch( err => {
      this.conductorQuartzMappingError = JSON.stringify(err);
    });

    this.sprinner.hide();
  }

  async setconductorQuartzMappingJsonTemp(conductorQuartzMapping: any)
  {
    if(undefined!= conductorQuartzMapping && ''!=conductorQuartzMapping)
    {
      this.conductorQuartzMappingJsonTemp = conductorQuartzMapping.scheduleWorkflowPayload;
    }
    else{
      this.conductorQuartzMappingJsonTemp = undefined;
    }
  }

  openLarge(content) {
    this.modalService.open(content, {
      size: 'lg'
    });
  }

  async viewScheduleHistory(scheduleId: string)
  {
    await localStorage.setItem('conductor.scheduleId', scheduleId);
    await this.router.navigateByUrl('quartz/view/view-schedule-history', { skipLocationChange: true }).then((fulfilled: boolean) => {
      console.log('Routed')
    })
  }

  async pauseWorkflowExecution(scheduleCounter: number, scheduleId: string)
  {
    this.sprinner.show();
    await this.schedulerApiCallerService.pauseWorkflowExecution(scheduleId).then((response: SchedulerResponse) => {
      if(response.message == "SUCCESS")
      {
        this.conductorQuartzMappingList[scheduleCounter].scheduleCurrentStatus = "PAUSED";
        this.conductorQuartzMappingActionSuccess = "Schedule PAUSED Successfully !!!";
        const messageTimeout = setTimeout(()=>{
          this.conductorQuartzMappingActionSuccess = undefined;
          clearTimeout(messageTimeout);
        }, 5000);
      }
    }).catch(err => {
      this.conductorQuartzMappingActionError = "Schedule Pause FAILED !!! Reason: "+ JSON.stringify(err)
      const messageTimeout = setTimeout(()=>{
        this.conductorQuartzMappingActionError = undefined;
        clearTimeout(messageTimeout);
      }, 5000);
    });
    this.sprinner.hide();
  }

  async resumeWorkflowExecution(scheduleCounter: number, scheduleId: string)
  {
    this.sprinner.show();
    await this.schedulerApiCallerService.resumeWorkflowExecution(scheduleId).then((response: SchedulerResponse) => {
      if(response.message == "SUCCESS")
      {
        this.conductorQuartzMappingList[scheduleCounter].scheduleCurrentStatus = "SCHEDULED";
        this.conductorQuartzMappingActionSuccess = "Schedule RESUMED Successfully !!!";
        const messageTimeout = setTimeout(()=>{
          this.conductorQuartzMappingActionSuccess = undefined;
          clearTimeout(messageTimeout);
        }, 5000);
      }
    }).catch(err => {
      this.conductorQuartzMappingActionError = "Schedule Resume FAILED !!! Reason: "+ JSON.stringify(err)
      const messageTimeout = setTimeout(()=>{
        this.conductorQuartzMappingActionError = undefined;
        clearTimeout(messageTimeout);
      }, 5000);
    });
    this.sprinner.hide();
  }

  async deleteWorkflowExecution(scheduleCounter: number, scheduleId: string)
  {
    this.sprinner.show();
    await this.schedulerApiCallerService.workflowExecutionSchedule(scheduleId).then((response: SchedulerResponse) => {
      if(response.message == "SUCCESS")
      {
        this.conductorQuartzMappingList.splice(scheduleCounter, 1);
        this.conductorQuartzMappingActionSuccess = "Schedule DELETED Successfully !!!";
        const messageTimeout = setTimeout(()=>{
          this.conductorQuartzMappingActionSuccess = undefined;
          clearTimeout(messageTimeout);
        }, 5000);
      }
    }).catch(err => {
      this.conductorQuartzMappingActionError = "Schedule Delete FAILED !!! Reason: "+ JSON.stringify(err)
      const messageTimeout = setTimeout(()=>{
        this.conductorQuartzMappingActionError = undefined;
        clearTimeout(messageTimeout);
      }, 5000);
    });
    this.sprinner.hide();
  }

}
