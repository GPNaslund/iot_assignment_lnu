/**
 * DTO containing a valid start and end date representing a 
 * date range.
 */
export class DateRange {
    public startDate: Date;
    public endDate: Date;

    /**
     * Creates a new instance of a DateRange.
     * @param startDate - The start date to store.
     * @param endDate - The end date to store.
     */
    constructor(startDate: Date, endDate: Date) {
        this.validateDateRange(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Helper method for validating the start and end dates relationship.
     * @param startDate - The date instance to compare.
     * @param endDate - The date instance to compare.
     */
    private validateDateRange(startDate: Date, endDate: Date) {
        if (endDate.getTime() < startDate.getTime()) {
            throw new Error("Start date cannot be after end date");
        }
    }
}