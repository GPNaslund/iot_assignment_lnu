import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';

/**
 * Component that displays a number and a corresponding symbol
 * with a retro digital font.
 *
 * @export
 * @class NumberDisplayComponent
 * @typedef {NumberDisplayComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-number-display',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './number-display.component.html',
  styleUrl: './number-display.component.css'
})

export class NumberDisplayComponent implements OnInit {
  @Input() value!: number;
  @Input() label!: string;
  @Input() symbol!: string;
  @Input() thresholdMin!: number;
  @Input() thresholdMax!: number;

  /**
   * Method to check if value is below set threshold.
   * @returns - boolean
   */
  isBelowThreshold(): boolean {
    return this.value < this.thresholdMin;
  }

  /**
   * Method to check if value is above set threshold.
   * @returns - boolean
   */
  isAboveThreshold(): boolean {
    return this.value > this.thresholdMax;
  }

  /**
   * Lifecycle method that runs when component initializes.
   */
  ngOnInit(): void {
    this.validateInput();
  }

  /**
   * Helper method for validating input variables.
   */
  private validateInput() {
    const errorStartStr = "Number display component: ";
    if (this.value == undefined) {
      throw new Error(errorStartStr + "value input cannot be undefined");
    }
    if (this.label == undefined) {
      throw new Error(errorStartStr + "label input cannot be undefined");
    }
    if (this.symbol == undefined) {
      throw new Error(errorStartStr + "symbol input cannot be undefined");
    }
    if (this.thresholdMax == undefined) {
      throw new Error(errorStartStr + "threshold max input cannot be undefined");
    }
    if (this.thresholdMin == undefined) {
      throw new Error(errorStartStr + "threshold min input cannot be undefined");
    }
  }
}


