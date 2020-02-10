import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { ScheduleWorkflowRequest } from '../Modals/ScheduleWorkflowRequest';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { SchedulePortfolioStats } from '../Modals/SchedulePortfolioStats';
import { SchedulerResponse } from '../Modals/SchedulerResponse';
import { ConductorQuartzMapping } from '../Modals/ConductorQuartzMapping';
import { ConductorQuartzHistory } from '../Modals/ConductorQuartzHistory';

@Injectable({
  providedIn: 'root'
})
export class SchedulerApiCallerService {
  
  private baseConductorSchedulerApiEndpoint: string;

  constructor(private http: HttpClient) {

    this.baseConductorSchedulerApiEndpoint = environment.WF_SCHEDULER;

   }

   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  } 
  
   async scheduleWorkflow(scheduleWorkflowRequest: ScheduleWorkflowRequest)
   {
     console.log('Schedule Workflow ...', (JSON.stringify(scheduleWorkflowRequest)));

     return await this.http.post(this.baseConductorSchedulerApiEndpoint + 'scheduleWorkflowExecution',JSON.stringify(scheduleWorkflowRequest), this.httpOptions).toPromise();
   }

   async workflowScheduleExecutionHistory(scheduleId: string)
   {
     console.log('Workflow Schedules Run History Get Request ...', scheduleId);

     return await this.http.get<ConductorQuartzHistory[]>(this.baseConductorSchedulerApiEndpoint + 'workflowScheduleExecutionHistory?scheduleId='+scheduleId, this.httpOptions).toPromise();
   }

   async pauseWorkflowExecution(scheduleId: string)
   {
     console.log('Pause Schedule Get Request ...', scheduleId);

     return await this.http.get<SchedulerResponse>(this.baseConductorSchedulerApiEndpoint + 'pauseWorkflowExecution?scheduleId='+scheduleId, this.httpOptions).toPromise();
   }

   async resumeWorkflowExecution(scheduleId: string)
   {
     console.log('Resume Schedule Get Request ...', scheduleId);

     return await this.http.get<SchedulerResponse>(this.baseConductorSchedulerApiEndpoint + 'resumeWorkflowExecution?scheduleId='+scheduleId, this.httpOptions).toPromise();
   }

   async workflowExecutionSchedule(scheduleId: string)
   {
     console.log('Delete Schedule Get Request ...', scheduleId);

     return await this.http.delete<SchedulerResponse>(this.baseConductorSchedulerApiEndpoint + 'workflowExecutionSchedule?scheduleId='+scheduleId, this.httpOptions).toPromise();
   }

   async schedulerPortfolioStats()
   {
     console.log('Schedule Portfolio Statistics Get Request ...');

     return await this.http.get<SchedulePortfolioStats>(this.baseConductorSchedulerApiEndpoint + 'schedulerPortfolioStats', this.httpOptions).toPromise();
   }

   async allWorkflowExecutionSchedules()
   {
     console.log('All Workflow Schedules Get Request ...');

     return await this.http.get<ConductorQuartzMapping[]>(this.baseConductorSchedulerApiEndpoint + 'allWorkflowExecutionSchedules', this.httpOptions).toPromise();
   }
}
