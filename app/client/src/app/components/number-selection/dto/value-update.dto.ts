/**
 * DTO that holds the three values selectable by the
 * NumberSelectionComponent.
 */
export class ValueUpdateDTO {
    public upperThreshold: number;
    public lowerThreshold: number;
    public preferredValue: number;

    /**
     * Creates a new instance of ValueUpdateDTO.
     * @param upperThreshold - Upper threshold value number.
     * @param lowerThreshold - Lower threshold value number.
     * @param preferredValue - Preferred value number.
     */
    constructor(upperThreshold: number, lowerThreshold: number, preferredValue: number) {
        this.upperThreshold = upperThreshold;
        this.lowerThreshold = lowerThreshold;
        this.preferredValue = preferredValue;
    }
}

