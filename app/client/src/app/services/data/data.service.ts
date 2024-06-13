import { Inject, Injectable } from '@angular/core';
import { IDataModel } from '../../model/data/data.interface';
import { DataModel } from '../../model/data/data.model';
import { TimePeriod } from '../../model/data/time-period';
import { DateRange } from '../dto/date-range';
import { ISensorDataAccess } from '../../data-access/sensor/sensor.data-access.interface';
import { SensorDataAccess } from '../../data-access/sensor/sensor.data-access';
import { ISensorData } from '../../model/sensor-data/sensor-data.interface';

/**
 * Service class for handling sensor data related functionality.
 *
 * @export
 * @class DataService
 * @typedef {DataService}
 */
@Injectable({
  providedIn: 'root'
})

export class DataService {
  private model: IDataModel;
  private dataAccess: ISensorDataAccess;

  /**
   * Creates a new instance of DataService.
   * @param model - The data model holding time period constraints.
   * @param dataAccess - The data access that queries for data.
   */
  constructor(@Inject(DataModel) model: IDataModel, @Inject(SensorDataAccess) dataAccess: ISensorDataAccess) {
    this.model = model;
    this.dataAccess = dataAccess;
  }

  /**
   * Method for getting valid time period alternatives in string format.
   * @returns - Array of strings representing valid time period alternatives.
   */
  getTimePeriodAlternatives(): Array<string> {
    return this.model.getTimePeriodsStrings();
  }

  /**
   * Method for getting a time period based on a string representation.
   * @param input - The string representing a time period.
   * @returns - A valid time period or undefined if string is not a valid representation
   * of a time period.
   */
  getTimePeriodFromString(input: string): TimePeriod | undefined {
    return this.model.getTimePeriodFromString(input);
  }

  /**
   * Method for getting a DateRange containing valid start and end date based on
   * provided TimePeriod.
   * @param period - The time period to get a DateRange from.
   * @returns 
   */
  getDateRange(period: TimePeriod): DateRange {
    const startDate = this.model.getStartDate(period);
    const endDate = this.model.getEndDate(period);
    return new DateRange(startDate, endDate);
  }

  /**
   * Method for getting sensor data based on the time period, start and end dates from data
   * access instance.
   * @param period - The timeperiod selected.
   * @param startDate - The start date selected.
   * @param endDate - The end date selected.
   * @returns 
   */
  async getSensorData(period: TimePeriod, startDate: Date, endDate: Date): Promise<ISensorData[]> {
    switch (period) {
      case TimePeriod.SHORT_TERM: {
        const shortTermData = await this.dataAccess.getSensorData(period, startDate, endDate);
        return shortTermData;
      }
      case TimePeriod.LONG_TERM: {
        const longTermData = await this.dataAccess.getSensorData(period, startDate, endDate);
        return longTermData;
      }
    }
  }
}
