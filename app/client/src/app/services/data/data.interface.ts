import { TimePeriod } from "../../model/data/time-period";
import { ISensorData } from "../../model/sensor-data/sensor-data.interface";
import { DateRange } from "../dto/date-range";

export interface IDataService {
    getTimePeriodAlternatives(): Array<string>;
    getTimePeriodFromString(input: string): TimePeriod | undefined;
    getDateRange(period: TimePeriod): DateRange;
    getSensorData(period: TimePeriod, startDate: Date, endDate: Date): Promise<ISensorData[]>;
}