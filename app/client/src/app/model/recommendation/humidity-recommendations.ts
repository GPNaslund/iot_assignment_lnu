export enum HumidityRecommendation {
    GENERAL = "General",
    ALLERGIES = "Allergies",
    ASTHMA = "Asthma",
    BACTERIA_GROWTH_PREVENTION = "Bacteria growth prevention",
    DUST_MITE_PREVENTION = "Dust and mite prevention",
    WINTER_MONTHS = "Winter months",
    SUMMER_MONTHS = "Summer months",
    RESPIRATORY_CONDITION = "Respiratory condition",
}

export interface Humidity {
    upperThreshold: number;
    lowerThreshold: number;
    preferredValue: number;
}

// Holds all valid humidity recommendations.
export const humidityRecommendations: Record<HumidityRecommendation, Humidity> = {
    [HumidityRecommendation.GENERAL]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 40 },
    [HumidityRecommendation.ALLERGIES]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 45 },
    [HumidityRecommendation.ASTHMA]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 42.5 },
    [HumidityRecommendation.BACTERIA_GROWTH_PREVENTION]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 50 },
    [HumidityRecommendation.DUST_MITE_PREVENTION]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 42.5 },
    [HumidityRecommendation.WINTER_MONTHS]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 35 },
    [HumidityRecommendation.SUMMER_MONTHS]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 45 },
    [HumidityRecommendation.RESPIRATORY_CONDITION]: { upperThreshold: 60, lowerThreshold: 30, preferredValue: 40 },
}