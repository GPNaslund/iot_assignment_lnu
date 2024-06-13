import { Inject, Injectable } from '@angular/core';
import { IRecommendationModel } from '../../model/recommendation/recommendation.interface';
import { Temperature } from '../../model/recommendation/temperature-recommendations';
import { Humidity } from '../../model/recommendation/humidity-recommendations';
import { IRecommendationService } from './recommendation.interface';
import { RecommendationModel } from '../../model/recommendation/recommendation.model';

/**
 * Method for getting recommendation data.
 *
 * @export
 * @class RecommendationService
 * @typedef {RecommendationService}
 * @implements {IRecommendationService}
 */
@Injectable({
  providedIn: 'root'
})

export class RecommendationService implements IRecommendationService {
  private model: IRecommendationModel;

  /**
   * Creates a new instance of RecommendationService.
   * @param model 
   */
  constructor(@Inject(RecommendationModel) model: IRecommendationModel) {
    this.model = model;
  }

  /**
   * Method for getting all temperature recommendations in string format.
   * @returns - An array of strings representing temperature recommendations.
   */
  getTemperatureRecommendationStrings(): string[] {
    return this.model.getTemperatureRecommendationStrings();
  }

  /**
   * Method for getting all humidity recommendations in string format.
   * @returns - An array of strings representing humidity recommendations.
   */
  getHumidityRecommendationStrings(): string[] {
    return this.model.getHumidityRecommendationStrings();
  }

  /**
   * Method for getting a temperature recommendation by its string representation.
   * @param recommendation - The string representation of a temperature recommendation.
   * @returns - The corresponding Temperature or undefined if invalid string representation.
   */
  getTemperatureRecommendationByString(recommendation: string): Temperature | undefined {
    return this.model.getTemperatureRecommendationByString(recommendation);
  }


  /**
   * Method for getting a humidity recommendation by its string representation
   * @param recommendation - The string representation of a humidity recommendation.
   * @returns - The corresponding Humidity or undefined if invalid string representation.
   */
  getHumidityRecommendationByString(recommendation: string): Humidity | undefined {
    return this.model.getHumidityRecommendationByString(recommendation);
  }

}
