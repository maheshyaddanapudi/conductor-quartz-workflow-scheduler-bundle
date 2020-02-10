export class TriggerWorkflowRequest{
    name: string;
    version: number;
    correlationId: string;
    input: any;

    constructor(
        name: string,
        version: number,
        correlationId: string,
        input: any
    )
    {
        this.name = name;
        this.version = version;
        this.correlationId = correlationId;
        this.input = input;
    }
}