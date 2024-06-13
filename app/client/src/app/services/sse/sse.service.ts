/* eslint-disable no-prototype-builtins */
/* eslint-disable @typescript-eslint/no-explicit-any */

import { Injectable, NgZone } from '@angular/core';
import { ISseService } from './sse.interface';
import { Heartbeat } from '../dto/heartbeat';
import { SensorData } from '../dto/sensor-data';

/**
 * Service class which handles SSE connection and event handling.
 *
 * @export
 * @class SseService
 * @typedef {SseService}
 * @implements {ISseService}
 */
@Injectable({
  providedIn: 'root'
})

export class SseService implements ISseService {
  private eventSource!: EventSource;
  private zone: NgZone;

  /**
   * Creates a new instance of SseService.
   * @param zone - NgZone instance used to handle events inside the Angular zone. Important
   * to make angular react to events thats "outside" of angular.
   */
  constructor(zone: NgZone) {
    this.zone = zone;
  }

  /**
   * Creates a new EventSource / Connects to SSE endpoint.
   * @param url - The URL of the SSE endpoint.
   */
  init(url: string): void {
    if (url == undefined || url == "") {
      throw new Error("SSE Service: Url cannot be null or empty")
    }

    this.eventSource = new EventSource(url);
    this.eventSource.onopen = () => console.log("Event source opened");
    this.eventSource.onerror = (error) => console.log("Event source error: ", error);
  }

  /**
   * Method for setting up the onmessage callback on the event source. Handles the logic
   * of the application when SSE messages comes.
   * @param callback
   */
  onMessage(callback: (data: any) => void): void {
    this.eventSource.onmessage = (event) => {
      this.zone.run(() => {
        const json = JSON.parse(event.data);
        if (json.hasOwnProperty("heartbeat")) {
          const heartbeat = new Heartbeat("heartbeat");
          callback(heartbeat);
        } else if (json.hasOwnProperty("temperature") && json.hasOwnProperty("humidity")) {
          const sensorData = new SensorData(json["temperature"], json["humidity"]);
          callback(sensorData);
        } else {
          callback("Unkown event");
        }
      });
    };
  }

  /**
   * Method for closing SSE connection.
   */
  close(): void {
    if (this.eventSource) {
      console.log("Closing SSE service..");
      this.eventSource.close();
    }
  }
}
