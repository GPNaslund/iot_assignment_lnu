/* eslint-disable @typescript-eslint/no-explicit-any */
export interface ISseService {
    init(url: string): void;
    onMessage(callback: (data: any) => void): void;
    close(): void;
}