import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { ILedStatus } from "../../model/led/led-status.interface";
import { ILedDataAccess } from "./led.data-access.interface";

/**
 * Class with methods for getting and setting
 * LED state.
 *
 * @export
 * @class LedDataAccess
 * @typedef {LedDataAccess}
 * @implements {ILedDataAccess}
 */
@Injectable({
  providedIn: "root",
})

export class LedDataAccess implements ILedDataAccess {
  private ledStatusUrl: string;
  private ledControlUrl: string;
  private apiKey: string;

  /**
   * Creates a new instance of LedDataAccess.
   */
  constructor() {
    this.ledStatusUrl = environment.ledStatusEndpoint;
    this.ledControlUrl = environment.ledControlEndpoint;
    this.apiKey = environment.apiKey;
  }

  /**
   * Method that queries backend for LedStatus.
   * @returns - ILedStatus implementing data structure.
   */
  async getLedStatus(): Promise<ILedStatus> {
    const response = await fetch(this.ledStatusUrl);
    try {
      const result: ILedStatus = await response.json();
      return result;
    } catch (error) {
      console.error("Fetching led state got error: ", error);
      return { "state": "OFF" }
    }
  }

  /**
   * Method that posts a led status to the backend.
   * @param value - The new led status to be sent.
   */
  async sendLedCommand(value: ILedStatus): Promise<void> {
    console.log(JSON.stringify(value));
    const response = await fetch(this.ledControlUrl, {
      method: 'POST',
      headers: {
        "Content-Type": "application/json",
        "X-API-Key": this.apiKey,
      },
      body: JSON.stringify(value)
    });
    if (!response.ok) {
      throw new Error("failed to make post request");
    }
  }
}
