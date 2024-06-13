import { Humidity } from "../../model/recommendation/humidity-recommendations";
import { Temperature } from "../../model/recommendation/temperature-recommendations";

export interface IRecommendationService {
    getTemperatureRecommendationStrings(): string[];
    getHumidityRecommendationStrings(): string[];
    getTemperatureRecommendationByString(recommendation: string): Temperature | undefined;
    getHumidityRecommendationByString(recommendation: string): Humidity | undefined;
}