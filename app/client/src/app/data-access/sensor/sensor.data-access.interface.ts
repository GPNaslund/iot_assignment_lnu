import { TimePeriod } from "../../model/data/time-period";
import { ISensorData } from "../../model/sensor-data/sensor-data.interface";

export interface ISensorDataAccess {
    getSensorData(period: TimePeriod, startDate: Date, endDate: Date): Promise<ISensorData[]>;
}