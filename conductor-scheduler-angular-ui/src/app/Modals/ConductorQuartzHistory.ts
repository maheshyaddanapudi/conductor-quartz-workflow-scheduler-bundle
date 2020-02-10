import { ConductorQuartzMapping } from './ConductorQuartzMapping'

export class ConductorQuartzHistory {
    historyId: number
    conductorQuartzMapping: ConductorQuartzMapping
    conductorCorrelationId: string
    quartzExecutionStatus: boolean
    insertTimestamp: Date
    updateTimestamp: Date
    quartzExecutionLog?: string

    constructor(
        historyId: number,
        conductorQuartzMapping: ConductorQuartzMapping,
        conductorCorrelationId: string,
        quartzExecutionStatus: boolean,
        insertTimestamp: Date,
        updateTimestamp: Date,
        quartzExecutionLog?: string
    )
    {
        this.historyId = historyId;
		this.conductorQuartzMapping = conductorQuartzMapping;
		this.conductorCorrelationId = conductorCorrelationId;
		this.quartzExecutionStatus = quartzExecutionStatus;
		this.insertTimestamp = insertTimestamp;
        this.updateTimestamp = updateTimestamp;
        
        if(undefined!=quartzExecutionLog)
        {
		    this.quartzExecutionLog = quartzExecutionLog;
        }
    }
}