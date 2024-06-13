import { TimePeriod } from "./time-period";

export interface IDataModel {
    getTimePeriodsStrings(): Array<string>;
    getTimePeriodFromString(input: string): TimePeriod | undefined;
    getStartDate(period: TimePeriod): Date;
    getEndDate(period: TimePeriod): Date;
}