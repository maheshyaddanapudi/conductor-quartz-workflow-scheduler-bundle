export class TaskDef
{
    ownerApp: string;
    createdBy: string;
    updatedBy: string;
    name: string;
    description: string;
    timeoutSeconds: number;
    inputKeys: string[];
    outputKeys: string[];
    responseTimeoutSeconds: number;
    concurrentExecLimit: number;
    retryCount: number;
    createTime?: number;
    updateTime?: number;
    timeoutPolicy?: string;
    retryLogic?: string;
    retryDelaySeconds?: number;
    inputTemplate?: any;
    rateLimitPerFrequency?: number;
    rateLimitFrequencyInSeconds?: number;
    isolationGroupId?: string;
    executionNameSpace?: string;

    constructor(
        ownerApp: string,
        createdBy: string,
        updatedBy: string,
        name: string,
        description: string,
        timeoutSeconds: number,
        inputKeys: string[],
        outputKeys: string[],
        responseTimeoutSeconds: number,
        concurrentExecLimit: number,
        retryCount: number,
        createTime?: number,
        updateTime?: number,
        timeoutPolicy?: string,
        retryLogic?: string,
        retryDelaySeconds?: number,
        inputTemplate?: any,
        rateLimitPerFrequency?: number,
        rateLimitFrequencyInSeconds?: number,
        isolationGroupId?: string,
        executionNameSpace?: string,
    )
    {
        this.ownerApp = ownerApp;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.name = name;
        this.description = description;
        this.timeoutSeconds = timeoutSeconds;
        this.inputKeys = inputKeys;
        this.outputKeys = outputKeys;
        this.responseTimeoutSeconds = responseTimeoutSeconds;
        this.concurrentExecLimit = concurrentExecLimit;
        this.retryCount = retryCount;
        
        if(undefined != createTime)
        {
            this.createTime = createTime;
        }
        if(undefined != updateTime)
        {
            this.updateTime = updateTime;
        }
        if(undefined != timeoutPolicy)
        {
            this.timeoutPolicy = timeoutPolicy;
        }
        if(undefined != retryLogic)
        {
            this.retryLogic = retryLogic;
        }
        if(undefined != retryDelaySeconds)
        {
            this.retryDelaySeconds = retryDelaySeconds;
        }
        else
        {
            this.retryDelaySeconds = 1;
        }
        if(undefined != inputTemplate)
        {
            this.inputTemplate = inputTemplate;
        }
        if(undefined != rateLimitPerFrequency)
        {
            this.rateLimitPerFrequency = rateLimitPerFrequency;
        }
        if(undefined != rateLimitFrequencyInSeconds)
        {
            this.rateLimitFrequencyInSeconds = rateLimitFrequencyInSeconds;
        }
        if(undefined != isolationGroupId)
        {
            this.isolationGroupId = isolationGroupId;
        }
        if(undefined != executionNameSpace)
        {
            this.executionNameSpace = executionNameSpace;
        }
    }
}
