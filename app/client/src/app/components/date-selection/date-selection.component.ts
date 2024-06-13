import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

/**
 * Component for selecting date, emitts the date selected.
 *
 * @export
 * @class DateSelectionComponent
 * @typedef {DateSelectionComponent}
 */
@Component({
  selector: 'app-date-selection',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './date-selection.component.html',
  styleUrl: './date-selection.component.css'
})
export class DateSelectionComponent implements OnInit, OnChanges, AfterViewInit {
  @Input() label!: string;
  @Input() startDate!: Date;
  @Input() endDate!: Date;
  public startDateString!: string;
  public endDateString!: string;
  @Output() dateSelected = new EventEmitter<string>();
  dateControl = new FormControl();

  /**
   * Creates a new instance of DateSelectionComponent.
   */
  constructor() {
  }

  /**
   * Lifecycle method that runs when component initialize.
   */
  ngOnInit() {
    this.inputValidator();
    this.startDateString = this.startDate.toISOString().split('T')[0];
    this.endDateString = this.endDate.toISOString().split('T')[0];
    this.dateControl.valueChanges.subscribe((value) => {
      this.dateSelected.emit(value);
    })
  }

  ngAfterViewInit(): void {
    const dateInput = document.querySelector("#" + this.label);

    if (dateInput) {
      dateInput.addEventListener('keydown', (event) => {
        event.preventDefault();
      });

      dateInput.addEventListener('keypress', (event) => {
        event.preventDefault();
      });

      dateInput.addEventListener('paste', (event) => {
        event.preventDefault();
      });
    }

  }


  ngOnChanges(changes: SimpleChanges) {
    if (changes['startDate'] || changes['endDate']) {
      this.updateDateStrings();
      this.dateControl.setValue(null);
    }
  }

  private updateDateStrings() {
    this.startDateString = this.startDate.toISOString().split('T')[0];
    this.endDateString = this.endDate.toISOString().split('T')[0];
  }

  /**
   * Helper method for validating input variables.
   */
  private inputValidator() {
    const errorStartStr = "Date selection component: ";
    if (this.label == undefined) {
      throw new Error(errorStartStr + "label input cannot be undefined");
    }
    if (this.startDate == undefined) {
      throw new Error(errorStartStr + "start date input cannot be undefined");
    }
    if (this.endDate == undefined) {
      throw new Error(errorStartStr + "end date input cannot be undefined");
    }
  }
}
