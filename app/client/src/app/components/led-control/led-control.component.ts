import { Component, Inject, OnInit } from '@angular/core';
import { ILedControlService } from '../../services/led-control/led-control.interface';
import { LedControlService } from '../../services/led-control/led-control.service';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { LedState } from '../../services/dto/led-state';

/**
 * Radio switch component that interacts with LED state through
 * backend and mqtt server.
 *
 * @export
 * @class LedControlComponent
 * @typedef {LedControlComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-led-control',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './led-control.component.html',
  styleUrl: './led-control.component.css'
})

export class LedControlComponent implements OnInit {
  private service: ILedControlService;
  public isInitialized: boolean = false;
  public switchControl = new FormControl();


  /**
   * Creates a new instance of LedControlComponent.
   * @param service - Service class implementing ILedControlService interface.
   */
  constructor(@Inject(LedControlService) service: ILedControlService) {
    this.service = service;
    this.switchControl.valueChanges.subscribe((value) => {
      this.handleSwitchToggle(value);
    })
  }

  /**
   * Lifecycle method that runs on component initialization.
   */
  async ngOnInit(): Promise<void> {
    await this.checkLedStatus();
  }

  /**
   * Helper method for getting the LED's and reflecting that
   * in the UI.
   */
  private async checkLedStatus() {
    try {
      const state = await this.service.getLedState();
      this.switchControl.setValue(state.ledState);
      this.switchControl.enable();
      this.isInitialized = true;
    } catch (error) {
      console.error("Failed to get LED state: ", error);
    }
  }

  /**
   * Handles radio switch toggle, if initialized/switched by user
   * calls the backend to alter state.
   * @param value - The LED state value.
   * @returns - void
   */
  private handleSwitchToggle(value: boolean) {
    if (!this.isInitialized) {
      return;
    }
    const state = new LedState(value);
    try {
      this.service.sendLedCommand(state);
    } catch (error) {
      console.error("Failed to send LED command: ", error);
    }
  }
}