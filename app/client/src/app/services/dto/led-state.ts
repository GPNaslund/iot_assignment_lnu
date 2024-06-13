/**
 * DTO holding a boolean indicating the state of a LED.
 */
export class LedState {
    private _state: boolean;

    /**
     * Creates a new instance of LedState.
     * @param status - The status boolean to be stored.
     */
    constructor(status: boolean) {
        this._state = status;
    }


    /**
     * Getter for the stored state value.
     *
     * @readonly
     * @type {boolean}
     */
    get ledState(): boolean {
        return this._state;
    }

}