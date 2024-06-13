/**
 * Class containing MetricConfig data.
 */
export class MetricConfig {
    label: string = "default";
    value: number = 0;
    symbol: string = "default";
    thresholdMin: number = 0;
    thresholdMax: number = 0;
    preferredValue: number = 0;

    /**
     * Creates a new instance of MetricConfig.
     * @param label - The name to be displayed with the metric.
     * @param symbol - The symbol to be displayed with the metric.
     */
    constructor(label: string, symbol: string) {
        this.label = label;
        this.symbol = symbol;
    }
}