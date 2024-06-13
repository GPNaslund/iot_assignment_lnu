/**
 * DTO containing a string value that can be used to heartbeat a connection.
 */
export class Heartbeat {
    private _value: string;

    /**
     * Creates a new instance of Heartbeat.
     * @param value - The value to be stored.
     */
    constructor(value: string) {
        this._value = value;
    }


    /**
     * Getter for the stored value.
     *
     * @readonly
     * @type {string}
     */
    get value(): string {
        return this._value;
    }
}