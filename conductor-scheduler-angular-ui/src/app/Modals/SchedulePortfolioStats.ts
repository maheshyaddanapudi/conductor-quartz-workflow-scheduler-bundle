export class SchedulePortfolioStats {
    total: number;
    failed: number;
    success: number;
    percentage: number;

    constructor(
        total: number,
        failed: number,
        success: number,
        percentage: number,
    )
    {
        this.total = total;
        this.failed = failed;
        this.success = success;
        this.percentage = percentage;
    }
}