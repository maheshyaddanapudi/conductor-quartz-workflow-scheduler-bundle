export class WorkflowDef {
    createTime: any;
    name: string;
    description: string;
    version: number;
    tasks: any[];
    schemaVersion: number;
    restartable: boolean;
    workflowStatusListenerEnabled: boolean;
    inputParameters?: string[];
    outputParameters?: string[];
    
    constructor(
        createTime: any,
        name: string,
        description: string,
        version: number,
        tasks: any[],
        schemaVersion: number,
        restartable: boolean,
        workflowStatusListenerEnabled: boolean,
        inputParameters?: string[],
        outputParameters?: string[]
    )
    {
        this.createTime = createTime;
        this.name = name;
        this.description = description;
        this.version = version;
        this.tasks = tasks;
        this.schemaVersion = schemaVersion;
        this.restartable = restartable;
        this.workflowStatusListenerEnabled = workflowStatusListenerEnabled;
        
        if(undefined != inputParameters && inputParameters.length > 0)
        {
            this.inputParameters = inputParameters;
        }
        if(undefined != outputParameters && outputParameters.length > 0)
        {
            this.outputParameters = outputParameters;
        }
    }
}