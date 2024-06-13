import { LedState } from "../dto/led-state";

export interface ILedControlService {
    getLedState(): Promise<LedState>;
    sendLedCommand(state: LedState): Promise<void>;
}