package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.MatchWeights;

public interface MatchWeightsService {
    MatchWeights getWeights();
    void updateWeights(double skill, double condition);
}
