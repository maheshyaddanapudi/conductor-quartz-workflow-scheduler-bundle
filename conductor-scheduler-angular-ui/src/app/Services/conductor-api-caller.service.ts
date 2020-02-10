import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { WorkflowDef } from '../Modals/WorkflowDef';
import { TriggerWorkflowRequest } from '../Modals/TriggerWorkflowRequest';

@Injectable({
  providedIn: 'root'
})
export class ConductorApiCallerService {

  private baseConductorServerApiEndpoint: string;

  constructor(private http: HttpClient) {

    this.baseConductorServerApiEndpoint = environment.WF_SERVER;

   }


   httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  } 
  
  httpOptionsResponseTypeText = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': "text/plain",
    })
  } 

  async getAvailableWorkflowsList(executionNamespace?: string)
  {
     console.log('Getting list of Workflows Available ...')
     return await this.http.get<WorkflowDef[]>(this.baseConductorServerApiEndpoint + 'metadata/workflow', this.httpOptions).toPromise();
  }

  async getWorkflowDetail(workflowName: string, workflowVersion: string, executionNamespace?: string)
   {
      console.log('Getting Workflow Details Available ...', workflowName, workflowVersion)
      return await this.http.get<WorkflowDef>(this.baseConductorServerApiEndpoint + 'metadata/workflow/'+workflowName+'?version='+workflowVersion, this.httpOptions).toPromise();
   }
  
   async triggerWorkflow(triggerWorkflowRequest: TriggerWorkflowRequest, executionNamespace?: string)
   {
     console.log('Trigger Workflow ...', (JSON.stringify(triggerWorkflowRequest)));

     return await this.http.post(this.baseConductorServerApiEndpoint + 'workflow',JSON.stringify(triggerWorkflowRequest), this.httpOptionsResponseTypeText).toPromise();
   }
}
