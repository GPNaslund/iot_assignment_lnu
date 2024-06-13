import { Injectable } from "@angular/core";
import { Temperature, TemperatureRecommendation, temperatureRecommendations } from "./temperature-recommendations";
import { HumidityRecommendation, humidityRecommendations } from "./humidity-recommendations";
import { IRecommendationModel } from "./recommendation.interface";

/**
 * Class that holds valid recommendations.
 *
 * @export
 * @class RecommendationModel
 * @typedef {RecommendationModel}
 * @implements {IRecommendationModel}
 */
@Injectable({
    providedIn: 'root',
})

export class RecommendationModel implements IRecommendationModel {

    /**
     * Creates a new instance of RecommendationModel.
     */
    constructor() {
    }

    /**
     * Method for getting all TemperatureRecommendations in string format.
     * @returns - Array of strings.
     */
    getTemperatureRecommendationStrings(): string[] {
        return Object.values(TemperatureRecommendation);
    }

    /**
     * Method for getting all HumidityRecommendations in string format.
     * @returns - Array of strings.
     */
    getHumidityRecommendationStrings(): string[] {
        return Object.values(HumidityRecommendation);
    }

    /**
     * Method for getting a temperature recommendation by its string format.
     * @param recommendation - The string representation of a temperature recommendation.
     * @returns - The temperature recommendation if string is a valid representation.
     */
    getTemperatureRecommendationByString(recommendation: string): Temperature | undefined {
        const match = Object.entries(TemperatureRecommendation).find(([, value]) => value === recommendation);
        if (!match) {
            return undefined;
        }
        const key = match[0] as keyof typeof TemperatureRecommendation;
        return temperatureRecommendations[TemperatureRecommendation[key]];
    }

    /**
     * Method for getting humidity recommendation by its string format.
     * @param recommendation - The string representation of a humidity recommendation.
     * @returns - The humidity recommendation if string is a valid representation.
     */
    getHumidityRecommendationByString(recommendation: string): Temperature | undefined {
        const match = Object.entries(HumidityRecommendation).find(([, value]) => value === recommendation);
        if (!match) {
            return undefined;
        }
        const key = match[0] as keyof typeof HumidityRecommendation;
        return humidityRecommendations[HumidityRecommendation[key]];
    }

}
