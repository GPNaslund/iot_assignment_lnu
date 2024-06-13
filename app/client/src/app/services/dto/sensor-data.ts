/**
 * Class holding sensor data.
 */
export class SensorData {
    private _temperature: number;
    private _humidity: number;

    /**
     * Creates a new instance of SensorData.
     * @param temperature - The temperature to store.
     * @param humidity - The humidity to store.
     */
    constructor(temperature: number, humidity: number) {
        this._temperature = temperature;
        this._humidity = humidity;
    }


    /**
     * Getter for the stored temperature.
     *
     * @readonly
     * @type {number}
     */
    get temperature(): number {
        return this._temperature;
    }


    /**
     * Getter for the stored humidity.
     *
     * @readonly
     * @type {number}
     */
    get humidity(): number {
        return this._humidity;
    }

}