import { Component, OnInit } from '@angular/core';
import { CronOptions } from 'cron-editor';
import { DatePipe, formatDate } from '@angular/common';
import { TriggerWorkflowRequest } from 'src/app/Modals/TriggerWorkflowRequest';
import { ScheduleWorkflowRequest } from 'src/app/Modals/ScheduleWorkflowRequest';
import { HttpErrorResponse } from '@angular/common/http';
import { ConductorApiCallerService } from 'src/app/Services/conductor-api-caller.service';
import { SchedulerApiCallerService } from 'src/app/Services/scheduler-api-caller.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { WorkflowDef } from 'src/app/Modals/WorkflowDef';
import { TaskDef } from 'src/app/Modals/TaskDef';
import { NgxSpinnerService } from 'ngx-spinner';
import { SchedulerResponse } from 'src/app/Modals/SchedulerResponse';

@Component({
  selector: 'app-schedule-worlfkow',
  templateUrl: './schedule-worlfkow.component.html',
  styleUrls: []
})
export class ScheduleWorlfkowComponent  implements OnInit {

  public currentOrientation: string = 'horizontal';

  public cronExpression = '4 3 2 12 1/1 ? *';
  public isCronDisabled = false;
  public cronOptions: CronOptions = {
    formInputClass: 'form-control cron-editor-input',
    formSelectClass: 'form-control cron-editor-select',
    formRadioClass: 'cron-editor-radio',
    formCheckboxClass: 'cron-editor-checkbox',
    
    defaultTime: '10:00:00',
    use24HourTime: true,
    hideMinutesTab: false,
    hideHourlyTab: false,
    hideDailyTab: false,
    hideWeeklyTab: false,
    hideMonthlyTab: false,
    hideYearlyTab: false,
    hideAdvancedTab: true,
    hideSeconds: true,
    removeSeconds: false,
    removeYears: false,
  };

  workflowName: string = '';
  workflowVersion: string = '1';

  workflowDetails: WorkflowDef;

  workflowSubmittedId: string = '';
  workflowSubmittedError: string = '';

  disableTrigger: boolean = false;
  
  inputParamsFromUI: string[] = [];
  inputParamsForBE: string[] = [];

  scheduleWorkflowMessage: string = '';
  schedulerResponse: any;

  scheduleName: string;
  scheduleDesc: string;

  workflowJsonPayload: string;

  public startDate: Date;
  public endDate: Date;

  tasks: TaskDef[];

  constructor(private conductorApiCallerService: ConductorApiCallerService, private sprinner: NgxSpinnerService, private schedulerApiCallerService: SchedulerApiCallerService, private modalService: NgbModal) { }

  async ngOnInit() {

    this.workflowName = localStorage.getItem('conductor.triggerWorkflowName'); 
    this.workflowVersion = localStorage.getItem('conductor.triggerWorkflowVersion'); 

    await localStorage.removeItem('conductor.triggerWorkflowName');
    await localStorage.removeItem('conductor.triggerWorkflowVersion');
    this.sprinner.show();
    await this.getWorkflowDetails();
    this.sprinner.hide();
    this.prepareInputParamsFromUIAndForBE();

    this.scheduleWorkflowMessage = '';
  }

  async updateInputValues(index: number, value: string)
  {
    this.resetWorkflowJsonPayload();
    this.inputParamsFromUI[index]=value;
  }

  async prepareInputParamsFromUIAndForBE()
  {
    await this.workflowDetails.inputParameters.forEach((element: string) => {
      this.inputParamsForBE.push(element);
      this.inputParamsFromUI.push(null);
    });
  }

  async getWorkflowDetails()
  {
    await this.conductorApiCallerService.getWorkflowDetail(this.workflowName, this.workflowVersion).then((workflowDetail: WorkflowDef) => {
      this.workflowDetails = workflowDetail;
    });

    this.tasks = this.workflowDetails.tasks;
  }

  async triggerWorkflow()
  {
    if(this.workflowJsonPayload == undefined)
    {
      let now = new Date();
      let generatedName: string = "TK-"+formatDate(now, 'dd_MM_yy_hh_mm_ss_SSS', 'en-US', '+0530');
      
      let finalInputParams: string = "{";
      let finalInputMap: Map<string, string> = new Map<string, string>();

      let loopCounter = 0;

      this.inputParamsFromUI.forEach((value: string) => {
        finalInputParams = finalInputParams + '"' + this.inputParamsForBE[loopCounter] + '":"' + value + '",' ;
        finalInputMap.set(this.inputParamsForBE[loopCounter] , value);
        loopCounter = loopCounter + 1;
      });

      if(finalInputParams.length > 1)
      {
        finalInputParams = finalInputParams.slice(0, finalInputParams.length-1);
      }

      finalInputParams = finalInputParams + "}";

      let triggerWorkflowRequest: TriggerWorkflowRequest = new TriggerWorkflowRequest(this.workflowName, Number.parseInt(this.workflowVersion), generatedName, JSON.parse(finalInputParams));

      this.sprinner.show();

      await this.conductorApiCallerService.triggerWorkflow(triggerWorkflowRequest).then((response: any) => {
        
      }).catch((err: HttpErrorResponse) => {

        if(err.status == 200)
        {
          this.workflowSubmittedId = err.error.text;
          this.workflowSubmittedError = '';
          this.disableTrigger = true;
        }
        else
        {
          try{
            this.workflowSubmittedId = '';
            this.workflowSubmittedError = err.error.error;
          }
          catch{
            this.workflowSubmittedId = '';
            this.workflowSubmittedError = err.message;
          }
        }
        
      });

      this.sprinner.hide();
    }
    else
    {
      let triggerWorkflowRequest: TriggerWorkflowRequest = JSON.parse(this.workflowJsonPayload);

      this.sprinner.show();

      await this.conductorApiCallerService.triggerWorkflow(triggerWorkflowRequest).then((response: any) => {
        
      }).catch((err: HttpErrorResponse) => {

        if(err.status == 200 || err.status == 204)
        {
          this.workflowSubmittedId = err.error.text;
          this.workflowSubmittedError = '';
          this.disableTrigger = true;
        }
        else
        {
          try{
            this.workflowSubmittedId = '';
            this.workflowSubmittedError = err.error.error;
          }
          catch{
            this.workflowSubmittedId = '';
            this.workflowSubmittedError = err.message;
          }
        }
        
      });

      this.sprinner.hide();
    }
  }

  async resetWorkflowJsonPayload()
  {
    this.workflowJsonPayload = undefined;
  }

  async scheduleWorkflow(cronExpression: string)
  {
    if(undefined == this.workflowJsonPayload)
    {
      let now = new Date();
      let generatedName: string = "TK-"+formatDate(now, 'dd_MM_yy_hh_mm_ss_SSS', 'en-US', '+0530');
      
      let finalInputParams: string = "{";
      let finalInputMap: Map<string, string> = new Map<string, string>();

      let loopCounter = 0;

      this.inputParamsFromUI.forEach((value: string) => {
        finalInputParams = finalInputParams + '"' + this.inputParamsForBE[loopCounter] + '":"' + value + '",' ;
        finalInputMap.set(this.inputParamsForBE[loopCounter] , value);
        loopCounter = loopCounter + 1;
      });

      if(finalInputParams.length > 1)
      {
        finalInputParams = finalInputParams.slice(0, finalInputParams.length-1);
      }

      finalInputParams = finalInputParams + "}";
      
      let triggerWorkflowRequest: TriggerWorkflowRequest = new TriggerWorkflowRequest(this.workflowName, Number.parseInt(this.workflowVersion), generatedName, JSON.parse(finalInputParams));

      var datePipe = new DatePipe('en-US');

      let scheduleWorkflowRequest: ScheduleWorkflowRequest = new ScheduleWorkflowRequest(this.workflowName, Number.apply(this.workflowVersion), this.scheduleName, JSON.stringify(triggerWorkflowRequest), cronExpression, datePipe.transform(this.endDate, 'yyyy-MM-dd HH:mm:ss.S'), this.scheduleDesc , datePipe.transform(this.startDate, 'yyyy-MM-dd HH:mm:ss.S'))

      this.sprinner.show();

      await this.schedulerApiCallerService.scheduleWorkflow(scheduleWorkflowRequest).then((response: SchedulerResponse) => {
        this.scheduleWorkflowMessage = JSON.stringify(response);
        this.disableTrigger = true;
      }).catch((err) => {
        this.scheduleWorkflowMessage = JSON.stringify(err);
        const scheduleWorkflowMessageTimeout = setTimeout(()=>{
          this.scheduleWorkflowMessage = '';
          clearTimeout(scheduleWorkflowMessageTimeout);
        }, 5000);
      });
      
      this.sprinner.hide();
    }
    else
    {
      let triggerWorkflowRequest: TriggerWorkflowRequest = JSON.parse(this.workflowJsonPayload);

      var datePipe = new DatePipe('en-US');

      let scheduleWorkflowRequest: ScheduleWorkflowRequest = new ScheduleWorkflowRequest(this.workflowName, Number.apply(this.workflowVersion), this.scheduleName, JSON.stringify(triggerWorkflowRequest), cronExpression, datePipe.transform(this.endDate, 'yyyy-MM-dd HH:mm:ss.S'), this.scheduleDesc, datePipe.transform(this.startDate, 'yyyy-MM-dd HH:mm:ss.S'))

      this.sprinner.show();

      await this.schedulerApiCallerService.scheduleWorkflow(scheduleWorkflowRequest).then((response: any) => {
        this.scheduleWorkflowMessage = JSON.stringify(response);

        this.disableTrigger = true;
      }).catch((err) => {
        this.scheduleWorkflowMessage = JSON.stringify(err);
        const scheduleWorkflowMessageTimeout = setTimeout(()=>{
          this.scheduleWorkflowMessage = '';
          clearTimeout(scheduleWorkflowMessageTimeout);
        }, 5000);
      });
      
      this.sprinner.hide();
    }
  }

  openLarge(content) {
    this.modalService.open(content, {
      size: 'lg'
    });
  }
}
