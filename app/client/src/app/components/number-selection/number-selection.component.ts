import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ValueUpdateDTO } from './dto/value-update.dto';

/**
 * Component with sliders for selecting preferred values, upper threshold
 * and lower threshold. Will synchronize to maintain relationships between
 * the values.
 *
 * @export
 * @class NumberSelectionComponent
 * @typedef {NumberSelectionComponent}
 */
@Component({
  selector: 'app-number-selection',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './number-selection.component.html',
  styleUrl: './number-selection.component.css'
})

export class NumberSelectionComponent implements OnInit, OnDestroy, OnChanges {
  @Input() label!: string;
  @Input() upperThreshold!: number;
  @Input() lowerThreshold!: number;
  @Input() preferredValue!: number;

  @Output() valueUpdate = new EventEmitter<ValueUpdateDTO>();
  displayForm: boolean = false;
  debounceTimeout!: any;

  upperThresholdControl!: FormControl;
  lowerThresholdControl!: FormControl;
  preferredValueControl!: FormControl;

  /**
   * Lifecycle method that runs when component initializes.
   */
  ngOnInit() {
    this.validateInput();
    this.upperThresholdControl = new FormControl(this.upperThreshold);
    this.lowerThresholdControl = new FormControl(this.lowerThreshold);
    this.preferredValueControl = new FormControl(this.preferredValue);
    this.handleLowerThresholdChanges();
    this.handleUpperThresholdChanges();
    this.handlePreferredValueChanges();
  }

  /**
   * Helper method for validating input variables.
   */
  private validateInput() {
    const errorStartStr = "Number selection component: ";
    if (this.label == undefined) {
      throw new Error(errorStartStr + "label input cannot be undefined");
    }
    if (this.upperThreshold == undefined) {
      throw new Error(errorStartStr + "upper threshold input cannot be undefined");
    }
    if (this.lowerThreshold == undefined) {
      throw new Error(errorStartStr + "lower threshold input cannot be undefined");
    }
    if (this.preferredValue == undefined) {
      throw new Error(errorStartStr + "preferred input cannot be undefined");
    }
  }

  /**
   * Lifecycle method that runs when component is about to get removed.
   */
  ngOnDestroy(): void {
    if (this.debounceTimeout) {
      clearTimeout(this.debounceTimeout);
    }
  }

  /**
   * Lifecycle method that runs when changes happend to the component for updating
   * the UI. Updates the UI on changes.
   * @param changes - SimpleChanges instance that holds a map with the changes that happend.
   */
  ngOnChanges(changes: SimpleChanges) {
    if (changes['upperThreshold'] && !changes['upperThreshold'].isFirstChange()) {
      this.upperThresholdControl.setValue(this.upperThreshold, { emitEvent: false });
    }
    if (changes['lowerThreshold'] && !changes['lowerThreshold'].isFirstChange()) {
      this.lowerThresholdControl.setValue(this.lowerThreshold, { emitEvent: false });
    }
    if (changes['preferredValue'] && !changes['preferredValue'].isFirstChange()) {
      this.preferredValueControl.setValue(this.preferredValue, { emitEvent: false });
    }
  }

  /**
   * Method that controls if the component is visible or not.
   */
  toggleForm() {
    this.displayForm = !this.displayForm;
  }

  /**
   * Logic for when lower threshold value changes. Maintains relationsship
   * between the values.
   */
  handleLowerThresholdChanges() {
    this.lowerThresholdControl.valueChanges.subscribe((value) => {
      if (value >= this.upperThreshold) {
        const newVal = this.upperThreshold - 1;
        this.lowerThresholdControl.setValue(newVal, { emitEvent: false });
        this.lowerThreshold = newVal;
      } else {
        this.lowerThreshold = value;
      }

      if (this.preferredValue < this.lowerThreshold) {
        this.preferredValueControl.setValue(this.lowerThreshold, { emitEvent: false });
        this.preferredValue = this.lowerThreshold;
      }

      this.emitUpdatedValues();
    });
  }

  /**
   * Logic for when upper threshold value changes. Maintains relationship between the
   * values.
   */
  handleUpperThresholdChanges() {
    this.upperThresholdControl.valueChanges.subscribe((value) => {
      if (value <= this.lowerThreshold) {
        const newVal = this.lowerThreshold + 1;
        this.upperThresholdControl.setValue(newVal, { emitEvent: false });
        this.upperThreshold = newVal;
      } else {
        this.upperThreshold = value;
      }

      if (this.preferredValue > this.upperThreshold) {
        this.preferredValueControl.setValue(this.upperThreshold, { emitEvent: false });
        this.preferredValue = this.upperThreshold;
      }

      this.emitUpdatedValues();
    });
  }

  /**
   * Logic for when preferred value changes. Maintains relationship between the
   * values.
   */
  handlePreferredValueChanges() {
    this.preferredValueControl.valueChanges.subscribe((value) => {
      if (value >= this.upperThreshold) {
        const newVal = this.upperThreshold;
        this.preferredValueControl.setValue(newVal, { emitEvent: false });
        this.preferredValue = newVal;
      } else if (value <= this.lowerThreshold) {
        const newVal = this.lowerThreshold;
        this.preferredValueControl.setValue(newVal, { emitEvent: false });
        this.preferredValue = newVal;
      } else {
        this.preferredValue = value;
      }
      this.emitUpdatedValues();
    });
  }

  emitUpdatedValues(): void {
    if (this.debounceTimeout) {
      clearTimeout(this.debounceTimeout)
    }
    this.debounceTimeout = setTimeout(() => {
      this.valueUpdate.emit(new ValueUpdateDTO(this.upperThreshold, this.lowerThreshold, this.preferredValue));
    }, 1000);
  }
}

