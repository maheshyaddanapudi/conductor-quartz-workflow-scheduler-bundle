import { Component, OnInit } from '@angular/core';
import { ModalDismissReasons, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { WorkflowDef } from 'src/app/Modals/WorkflowDef';
import { ConductorApiCallerService } from 'src/app/Services/conductor-api-caller.service';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-view-all-workflows',
  templateUrl: './view-all-workflows.component.html',
  styleUrls: []
})
export class ViewAllWorkflowsComponent implements OnInit {

  workflowDefs: WorkflowDef[];
  searchString: string;

  JSON: JSON;

  workflowDefJsonTemp: string;

  constructor(public router: Router, private sprinner: NgxSpinnerService, private conductorApiCallerService: ConductorApiCallerService, private modalService: NgbModal) { 

    this.workflowDefs = [];
    this.JSON = JSON;
    
  }

  async ngOnInit() {

    this.workflowDefJsonTemp = '';

    this.sprinner.show();

    await this.conductorApiCallerService.getAvailableWorkflowsList().then((response: WorkflowDef[]) => {

      this.workflowDefs = response;

      console.log(JSON.stringify(this.workflowDefs));

    }).catch((error) => {
      console.log(error);
    });

    this.sprinner.hide();

  }

  async setworkflowDefJsonTemp(tempJson: any){
    if(undefined == tempJson || '' == tempJson)
    {
      this.workflowDefJsonTemp = '';
    }
    else{
      this.workflowDefJsonTemp = JSON.stringify(tempJson);
    }
  }

  openSmall(content) {
    this.modalService.open(content, {
      size: 'sm'
    });
  }

  openLarge(content) {
    this.modalService.open(content, {
      size: 'lg'
    });
  }

  async viewWorkflowDetails(workflowName: string, workflowVersion: string)
  {
    await localStorage.setItem("TRIGGER_WORKFLOW_NAME", workflowName);
    await localStorage.setItem("TRIGGER_WORKFLOW_VERSION", workflowVersion);

    await this.router.navigateByUrl('conductor/trigger/trigger-workflow', { skipLocationChange: true }).then((fulfilled: boolean) => {
      console.log('Routed')
      });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  async triggerWorkflow(workflowName: string, workflowVersion: string)
  {
    await localStorage.setItem('conductor.triggerWorkflowName', workflowName);
    await localStorage.setItem('conductor.triggerWorkflowVersion', workflowVersion);

    this.router.navigateByUrl('conductor/schedule/schedule-workflow', { skipLocationChange: true }).then((fulfilled: boolean) => {
      console.log('Routed')
    })
  }
}
