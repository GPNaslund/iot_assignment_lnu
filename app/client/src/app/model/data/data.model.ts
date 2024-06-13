import { Injectable } from "@angular/core";
import { TimePeriod } from "./time-period";

/**
 * Model class that holds the available time periods and
 * valid start + end date based on the time period.
 *
 * @export
 * @class DataModel
 * @typedef {DataModel}
 */
@Injectable({
    providedIn: "root",
})

export class DataModel {

    /**
     * Creates a new instance of DataModel.
     */
    constructor() {
    }

    /**
     * Method that returns the available time periods in string form.
     * @returns - Array with TimePeriod's in string format.
     */
    getTimePeriodsStrings(): Array<string> {
        return Object.values(TimePeriod);
    }

    /**
     * Method for getting a time period from its string format.
     * @param input - The string to get a time period from.
     * @returns - The corresponding TimePeriod or undefined if string is not a valid
     * time period.
     */
    getTimePeriodFromString(input: string): TimePeriod | undefined {
        if (Object.values(TimePeriod).includes(input as TimePeriod)) {
            return input as TimePeriod;
        } else {
            return undefined;
        }
    }

    /**
     * Method for getting a valid start date from provided
     * time period.
     * @param period - The time period to get a valid start date from. 
     * @returns - The valid start date.
     */
    getStartDate(period: TimePeriod): Date {
        const date = new Date();
        switch (period) {
            case TimePeriod.SHORT_TERM: {
                const daysRealTime = date.getDate() - 2;
                date.setDate(daysRealTime);
                return date;
            }
            case TimePeriod.LONG_TERM: {
                const daysLongTerm = date.getDate() - 364;
                date.setDate(daysLongTerm);
                return date;
            }
        }
    }

    /**
     * Method for getting a valid end date from provided
     * time period.
     * @param period - The time period to get a valid end date from.
     * @returns 
     */
    getEndDate(period: TimePeriod): Date {
        switch (period) {
            case TimePeriod.SHORT_TERM:
                return new Date();
            case TimePeriod.LONG_TERM:
                return new Date();
        }
    }
}