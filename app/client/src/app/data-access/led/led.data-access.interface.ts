import { ILedStatus } from "../../model/led/led-status.interface";

export interface ILedDataAccess {
    getLedStatus(): Promise<ILedStatus>;
    sendLedCommand(value: ILedStatus): Promise<void>;
}