import { Humidity } from "./humidity-recommendations";
import { Temperature } from "./temperature-recommendations";

export interface IRecommendationModel {
    getTemperatureRecommendationStrings(): string[];
    getHumidityRecommendationStrings(): string[];
    getTemperatureRecommendationByString(recommendation: string): Temperature | undefined;
    getHumidityRecommendationByString(recommendation: string): Humidity | undefined;
}