import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { TimePeriod } from "../../model/data/time-period";
import { ISensorData } from "../../model/sensor-data/sensor-data.interface";
import { ISensorDataAccess } from "./sensor.data-access.interface";

/**
 * Class with methods for querying the backend
 * for stored sensor data.
 *
 * @export
 * @class SensorDataAccess
 * @typedef {SensorDataAccess}
 * @implements {ISensorDataAccess}
 */
@Injectable({
  providedIn: "root"
})

export class SensorDataAccess implements ISensorDataAccess {
  private shorttermEndpointUrl: string;
  private longtermEndpointUrl: string;

  /**
   * Creates a new SensorDataAccess instance.
   */
  constructor() {
    this.shorttermEndpointUrl = environment.shortTermEndpoint;
    this.longtermEndpointUrl = environment.longTermEndpoint;
  }

  /**
   * Method for getting stored sensor data.
   * @param period - The time period to query the backend for (short or long term).
   * @param startDate - The start date for the query.
   * @param endDate - The end date for the query.
   * @returns
   */
  async getSensorData(period: TimePeriod, startDate: Date, endDate: Date): Promise<ISensorData[]> {
    let data;
    switch (period) {
      case TimePeriod.SHORT_TERM:
        data = await this.getShortTermData(startDate, endDate);
        return data;
      case TimePeriod.LONG_TERM:
        data = await this.getLongTermData(startDate, endDate);
        return data;
    }
  }

  /**
   * Helper method for querying backend for short term sensor data.
   * @param startDate - The start date to query from.
   * @param endDate - The end date to query to.
   * @returns
   */
  private async getShortTermData(startDate: Date, endDate: Date): Promise<ISensorData[]> {
    const url = this.constructUrl(this.shorttermEndpointUrl, startDate, endDate);
    const response = await fetch(url);
    const data: ISensorData[] = await response.json();
    return data;
  }

  /**
   * Helper method for querying backend for long term sensor data.
   * @param startDate - The start date to query from.
   * @param endDate - The end date to query to.
   * @returns
   */
  private async getLongTermData(startDate: Date, endDate: Date): Promise<ISensorData[]> {
    const url = this.constructUrl(this.longtermEndpointUrl, startDate, endDate);
    const response = await fetch(url);
    const data: ISensorData[] = await response.json();
    return data;
  }

  /**
   * Helper method for constructing the url to query, with query params.
   * @param urlStr - The base url.
   * @param startDate - The start date to query from.
   * @param endDate - The end date to query to.
   * @returns
   */
  private constructUrl(urlStr: string, startDate: Date, endDate: Date): URL {
    const url = new URL(urlStr);
    const startDateStr = startDate.toISOString().split('T')[0];
    const endDateStr = endDate.toISOString().split('T')[0];
    url.searchParams.append("start", startDateStr);
    url.searchParams.append("end", endDateStr);
    return url;
  }
}
