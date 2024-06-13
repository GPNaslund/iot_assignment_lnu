export enum TemperatureRecommendation {
    WHO_GENERAL = "WHO General",
    WHO_VERY_YOUNG = "WHO Very young",
    WHO_VERY_OLD = "WHO Very old",
    WHO_ILLNESS = "WHO Illness",
    ADEME_LIVING_ROOM = "ADEME Living room",
    ADEME_OLD = "ADEME Old",
    ADEME_BED_ROOM = "ADEME Bed room",
    FoHMFS_GENERAL = "FoHMS General",
}

export interface Temperature {
    upperThreshold: number;
    lowerThreshold: number;
    preferredValue: number;
}

// All valid temperature recommendations.
export const temperatureRecommendations: Record<TemperatureRecommendation, Temperature> = {
    [TemperatureRecommendation.WHO_GENERAL]: { upperThreshold: 24, lowerThreshold: 18, preferredValue: 21 },
    [TemperatureRecommendation.WHO_VERY_YOUNG]: { upperThreshold: 24, lowerThreshold: 20, preferredValue: 22 },
    [TemperatureRecommendation.WHO_VERY_OLD]: { upperThreshold: 24, lowerThreshold: 20, preferredValue: 22 },
    [TemperatureRecommendation.WHO_ILLNESS]: { upperThreshold: 24, lowerThreshold: 20, preferredValue: 22 },
    [TemperatureRecommendation.ADEME_LIVING_ROOM]: { upperThreshold: 22, lowerThreshold: 16, preferredValue: 19 },
    [TemperatureRecommendation.ADEME_OLD]: { upperThreshold: 24, lowerThreshold: 18, preferredValue: 21 },
    [TemperatureRecommendation.ADEME_BED_ROOM]: { upperThreshold: 20, lowerThreshold: 15, preferredValue: 17 },
    [TemperatureRecommendation.FoHMFS_GENERAL]: { upperThreshold: 24, lowerThreshold: 18, preferredValue: 22 },
}