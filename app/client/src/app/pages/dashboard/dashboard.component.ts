import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { NumberDisplayComponent } from '../../components/number-display/number-display.component';
import { NumberSelectionComponent } from '../../components/number-selection/number-selection.component';
import { ValueUpdateDTO } from '../../components/number-selection/dto/value-update.dto';
import { RecommendationSelectionComponent } from '../../components/recommendation-selection/recommendation-selection.component';
import { ISseService } from '../../services/sse/sse.interface';
import { environment } from '../../../environments/environment';
import { Heartbeat } from '../../services/dto/heartbeat';
import { SensorData } from '../../services/dto/sensor-data';
import { MetricConfig } from '../../model/metric/metric-config';
import { IRecommendationService } from '../../services/recommendation/recommendation.interface';
import { SseService } from '../../services/sse/sse.service';
import { RecommendationService } from '../../services/recommendation/recommendation.service';

/**
 * Dashboard component displayed as a page.
 *
 * @export
 * @class DashboardComponent
 * @typedef {DashboardComponent}
 * @implements {OnInit}
 * @implements {OnDestroy}
 */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NumberDisplayComponent, NumberSelectionComponent, RecommendationSelectionComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})


export class DashboardComponent implements OnInit, OnDestroy {
  private sseService: ISseService;
  private recommendationService: IRecommendationService;

  public temperature: MetricConfig;
  public humidity: MetricConfig;
  public temperatureRecommendationStrings!: string[];
  public humidityRecommendationStrings!: string[];

  /**
   * Creates a new instance of DashboardComponent.
   * @param sseService - The service class that handles the SSE connection.
   * @param recommendationService - Service class that handles the recommendation data.
   */
  constructor(@Inject(SseService) sseService: ISseService,
    @Inject(RecommendationService) recommendationService: IRecommendationService,
  ) {
    this.sseService = sseService;
    this.recommendationService = recommendationService;

    this.temperature = new MetricConfig("Temperature", "°C");
    this.humidity = new MetricConfig("Humidity", "%");
    this.temperatureRecommendationStrings = this.recommendationService.getTemperatureRecommendationStrings();
    this.humidityRecommendationStrings = this.recommendationService.getHumidityRecommendationStrings();
  }

  /**
   * Lifecycle method that gets called when component is initialized.
   * Sets up the event handling for messages coming from the SSE endpoint.
   */
  ngOnInit(): void {
    try {
      this.sseService.init(environment.sseEndpoint);
      this.sseService.onMessage((data) => {
        this.handleMessage(data);
      })
    } catch (error) {
      console.error(error);
    }
  }

  /**
   * Method for handling the incoming messages from the SSE endpoint.
   * @param data - The incoming data object from the service class handling the SSE
   * connection.
   */
  handleMessage(data: Heartbeat | SensorData | undefined) {
    if (data instanceof Heartbeat) {
      console.log("SSE endpoint heartbeat");
    } else if (data instanceof SensorData) {
      this.temperature.value = data.temperature;
      this.humidity.value = data.humidity;
    } else {
      console.log("Unkown event");
    }
  }

  /**
   * Lifecycle method that gets called before the component is destroyed.
   */
  ngOnDestroy(): void {
    this.sseService.close();
  }

  /**
   * Method for updating the local metric config holding
   * the temperature settings.
   * @param dto - The incoming dto.
   */
  temperatureSettingsUpdate(dto: ValueUpdateDTO) {
    this.temperature.preferredValue = dto.preferredValue;
    this.temperature.thresholdMax = dto.upperThreshold;
    this.temperature.thresholdMin = dto.lowerThreshold;
  }

  /**
   * Method for updating the local metric config holding
   * the humidity settings.
   * @param dto - The incoming dto.
   */
  humiditySettingsUpdate(dto: ValueUpdateDTO) {
    this.humidity.preferredValue = dto.preferredValue;
    this.humidity.thresholdMax = dto.upperThreshold;
    this.humidity.thresholdMin = dto.lowerThreshold;
  }

  /**
   * Method for updating the local metric config holding
   * temperature settings based on a selected temperature recommendation.
   * @param selection - String representation of the temperature recommendation made.
   */
  temperatureRecommendationMade(selection: string) {
    const temperatureValues = this.recommendationService.getTemperatureRecommendationByString(selection);
    if (temperatureValues != undefined) {
      this.temperature.thresholdMax = temperatureValues.upperThreshold;
      this.temperature.thresholdMin = temperatureValues.lowerThreshold;
      this.temperature.preferredValue = temperatureValues.preferredValue;
    } else {
      console.error("Undefined temperature recommendation made");
    }
  }

  /**
   * Method for updating the local metric config holding
   * humidity settings based on a selected humidity recommendation.
   * @param selection - String representation of the humidity recommendation made.
   */
  humidityRecommendationMade(selection: string) {
    const humidityValues = this.recommendationService.getHumidityRecommendationByString(selection);
    if (humidityValues != undefined) {
      this.humidity.thresholdMax = humidityValues.upperThreshold;
      this.humidity.thresholdMin = humidityValues.lowerThreshold;
      this.humidity.preferredValue = humidityValues.preferredValue;
    } else {
      console.error("Undefined humidity recommendation made");
    }
  }
}
