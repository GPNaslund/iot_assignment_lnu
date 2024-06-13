import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { DropdownSelectionComponent } from '../../components/dropdown-selection/dropdown-selection.component';
import { IDataService } from '../../services/data/data.interface';
import { DataService } from '../../services/data/data.service';
import { DateSelectionComponent } from '../../components/date-selection/date-selection.component';
import { CommonModule } from '@angular/common';
import { DateRange } from '../../services/dto/date-range';
import { ReactiveFormsModule } from '@angular/forms';
import { TimePeriod } from '../../model/data/time-period';
import { ISensorData } from '../../model/sensor-data/sensor-data.interface';
import { SensorChartComponent } from '../../components/sensor-chart/sensor-chart.component';

/**
 * Data component displayed as a page. Lets user select the
 * data to display in chart form.
 *
 * @export
 * @class DataComponent
 * @typedef {DataComponent}
 */
@Component({
  selector: 'app-data',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, DropdownSelectionComponent, DateSelectionComponent, SensorChartComponent],
  templateUrl: './data.component.html',
  styleUrl: './data.component.css'
})

export class DataComponent {
  private service: IDataService;
  private cdr: ChangeDetectorRef;

  public timePeriods: Array<string>;
  public timePeriodIsChoosen = false;
  selectedTimePeriod!: TimePeriod;

  public dateRange!: DateRange;

  public selectedStartDate!: Date;
  public startDateIsChoosen: boolean = false;

  public selectedEndDate!: Date;
  public endDateIsChoosen: boolean = false;


  public data!: ISensorData[];

  /**
   * Creates a new instance of DataComponent.
   * @param service - Service class that implements the IDataService interface.
   */
  constructor(@Inject(DataService) service: IDataService, cdr: ChangeDetectorRef) {
    this.service = service;
    this.timePeriods = this.service.getTimePeriodAlternatives();
    this.cdr = cdr;
  }

  /**
   * Method for handling selection of a time period.
   * @param value - The time period selected in string form.
   */
  timePeriodValueChanged(value: string) {
    this.startDateIsChoosen = false;
    this.endDateIsChoosen = false;
    this.timePeriodIsChoosen = false;
    const selectedTimePeriod = this.service.getTimePeriodFromString(value);
    if (selectedTimePeriod) {
      this.selectedTimePeriod = selectedTimePeriod;
      this.dateRange = this.service.getDateRange(selectedTimePeriod);
      this.timePeriodIsChoosen = true;
      this.cdr.detectChanges();
    } else {
      console.error("Undefined time period selected");
    }
  }

  /**
   * Method for handling the selection of a start date.
   * @param value - The date selection in string format.
   */
  startDateSelected(value: string) {
    if (value == null) {
      return;
    }
    try {
      this.selectedStartDate = new Date(value);
      this.startDateIsChoosen = true;
    } catch (error) {
      console.error("Invalid start date selected: ", error);
    }
  }

  /**
   * Method for handling the selection of an end date.
   * @param value - The date selection in string format.
   */
  endDateSelected(value: string) {
    if (value == null) {
      return;
    }
    try {
      this.selectedEndDate = new Date(value);
      this.endDateIsChoosen = true;
    } catch (error) {
      console.error("Invalid end date selected: ", error);
    }
  }

  /**
   * Method for getting the data selected data. SEtting the data variable
   * will trigger the display of a chart in the component.
   */
  async showDataClicked() {
    if (this.selectedStartDate && this.selectedEndDate) {
      try {
        this.data = await this.service.getSensorData(this.selectedTimePeriod, this.selectedStartDate, this.selectedEndDate);
      } catch (error) {
        console.error("Failed to get data: ", error);
      }
    }
  }

}
