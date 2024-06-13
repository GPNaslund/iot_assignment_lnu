import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

/**
 * Component for selecting temperature and humidity recommendations.
 * Emitts the selected recommendation.
 *
 * @export
 * @class RecommendationSelectionComponent
 * @typedef {RecommendationSelectionComponent}
 */
@Component({
  selector: 'app-recommendation-selection',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [],
  templateUrl: './recommendation-selection.component.html',
  styleUrl: './recommendation-selection.component.css'
})

export class RecommendationSelectionComponent implements OnInit {
  displayForm: boolean = false;
  @Input() humidityRecommendations!: Array<string>
  @Input() temperatureRecommendations!: Array<string>
  @Output() humidityRecommendationSelected = new EventEmitter<string>();
  @Output() temperatureRecommendationSelected = new EventEmitter<string>();
  humidityFormControl = new FormControl();
  temperatureFormControl = new FormControl();


  /**
   * Method that handles the visibility of the
   * component.
   */
  toggleForm() {
    this.displayForm = !this.displayForm;
  }

  /**
   * Lifecycle method that runs when component is initialized.
   */
  ngOnInit(): void {
    this.validateInput();

    this.humidityFormControl.valueChanges.subscribe((value) => {
      this.humidityRecommendationSelected.emit(value);
    })
    this.temperatureFormControl.valueChanges.subscribe((value) => {
      this.temperatureRecommendationSelected.emit(value);
    })
  }

  /**
   * Helper method for validating input variables.
   */
  private validateInput() {
    const errorStartStr = "Recommendation selection component: ";
    if (this.humidityRecommendations == undefined) {
      throw new Error(errorStartStr + "humidity recommendations input cannot be undefined");
    }
    if (this.temperatureRecommendations == undefined) {
      throw new Error(errorStartStr + "temperature recommendations input cannot be undefined");
    }
  }
}


