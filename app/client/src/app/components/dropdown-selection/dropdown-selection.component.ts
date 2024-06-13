import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

/**
 * Component for displaying a dropdown selection. 
 * Emitts event with the selection.
 *
 * @export
 * @class DropdownSelectionComponent
 * @typedef {DropdownSelectionComponent}
 */
@Component({
  selector: 'app-dropdown-selection',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dropdown-selection.component.html',
  styleUrl: './dropdown-selection.component.css'
})

export class DropdownSelectionComponent implements OnInit {
  @Input() label!: string;
  @Input() options!: Array<string>;
  @Output() optionSelected = new EventEmitter<string>();
  optionsControl = new FormControl();

  /**
   * Lifecycle method tha truns when component initialize.
   */
  ngOnInit() {
    this.inputValidator();
    this.optionsControl.valueChanges.subscribe((value) => {
      this.optionSelected.emit(value);
    })
  }

  /**
   * Helper method for validating input.
   */
  private inputValidator() {
    const errorStartStr = "Dropdown selection component: ";
    if (this.label == undefined) {
      throw new Error(errorStartStr + "label input cannot be undefined");
    }
    if (this.options == undefined) {
      throw new Error(errorStartStr + "options input cannot be undefined");
    }
  }
}
