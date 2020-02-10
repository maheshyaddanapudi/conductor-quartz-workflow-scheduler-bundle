export class SchedulerResponse {
    scheduleId: string;
    message: string;

    constructor(
        scheduleId: string,
        message: string
    )
    {
        this.scheduleId = scheduleId;
        this.message = message;
    }
}