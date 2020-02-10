export class ScheduleWorkflowRequest {

    workflowName: string;
    workflowVersion: number;
    scheduleName: string;
    json: string;
    cronExpression: string;
    endDate: string;
    startDate?; string;
    scheduleDesc?: string;

    constructor(workflowName:string, workflowVersion: number, scheduleName: string, json: string, cronExpression: string, endDate: string, scheduleDesc?: string, startDate?: string){
       
        this.workflowName = workflowName;
        this.workflowVersion = workflowVersion;
        this.scheduleName = scheduleName;
        this.json = json;
        this.cronExpression = cronExpression;
        this.endDate = endDate;

        if( startDate != undefined)
        {
            this.startDate = startDate;
        }

        if( scheduleDesc != undefined)
        {
            this.scheduleDesc = scheduleDesc;
        }
    }
}