import { Inject, Injectable } from '@angular/core';
import { ILedDataAccess } from '../../data-access/led/led.data-access.interface';
import { LedDataAccess } from '../../data-access/led/led.data-access';
import { LedState } from '../dto/led-state';

/**
 * Service class for getting and setting the LED state.
 *
 * @export
 * @class LedControlService
 * @typedef {LedControlService}
 */
@Injectable({
  providedIn: 'root'
})

export class LedControlService {
  private dataAccess: ILedDataAccess;

  /**
   * Creates a new instance of LedControlService.
   * @param dataAccess - Instance that implements the ILedDataAccess interface.
   */
  constructor(@Inject(LedDataAccess) dataAccess: ILedDataAccess) {
    this.dataAccess = dataAccess;
  }

  /**
   * Method for getting the stored led state from the data access.
   * @returns - A promise that resolves to a LedState.
   */
  public async getLedState(): Promise<LedState> {
    const ledStatus = await this.dataAccess.getLedStatus();
    switch (ledStatus.state) {
      case "ON":
        return new LedState(true);
      default:
        return new LedState(false);
    }
  }

  /**
   * Method for setting a new LedState to the data access.
   * @param state - The new state of the led to be stored.
   */
  public async sendLedCommand(state: LedState): Promise<void> {
    switch (state.ledState) {
      case true: {
        const onStatus = { state: "ON" };
        await this.dataAccess.sendLedCommand(onStatus);
        break;
      }
      case false: {
        const offStatus = { state: "OFF" };
        await this.dataAccess.sendLedCommand(offStatus);
        break;
      }
    }
  }
}
