export class ConductorQuartzMapping{
    mappingId: number
    quartzSchedulerId: string
    scheduleName: string
    scheduleDescription: string
    workflowName: string
    workflowVersion: number
    scheduleWorkflowPayload: string
    scheduleCronExpression: string
    scheduleCurrentStatus: string
    scheduleStartTimestamp: Date
    scheduleStopTimestamp: Date
    insertTimestamp: Date
    updateTimestamp: Date

    constructor(

        mappingId: number,
        quartzSchedulerId: string,
        scheduleName: string,
        scheduleDescription: string,
        workflowName: string,
        workflowVersion: number,
        scheduleWorkflowPayload: string,
        scheduleCronExpression: string,
        scheduleCurrentStatus: string,
        scheduleStartTimestamp: Date,
        scheduleStopTimestamp: Date,
        insertTimestamp: Date,
        updateTimestamp: Date
    ){
        this.mappingId = mappingId;
        this.quartzSchedulerId =quartzSchedulerId;
        this.scheduleName = scheduleName;
		this.scheduleDescription = scheduleDescription;
		this.workflowName = workflowName;
		this.workflowVersion = workflowVersion;
		this.scheduleWorkflowPayload = scheduleWorkflowPayload;
		this.scheduleCronExpression = scheduleCronExpression;
		this.scheduleCurrentStatus = scheduleCurrentStatus;
		this.scheduleStartTimestamp = scheduleStartTimestamp;
		this.scheduleStopTimestamp = scheduleStopTimestamp;
		this.insertTimestamp = insertTimestamp;
		this.updateTimestamp = updateTimestamp;
    }
}