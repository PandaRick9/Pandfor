package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.MatchWeights;
import by.baraznov.recruiting.repositories.MatchWeightsRepository;
import by.baraznov.recruiting.services.MatchWeightsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MatchWeightsServiceImpl implements MatchWeightsService {
    private final MatchWeightsRepository matchWeightsRepository;

    @Override
    public MatchWeights getWeights() {
        return matchWeightsRepository.findById(1).orElseGet(() -> {
            MatchWeights weights = new MatchWeights();
            weights.setSkillWeight(0.6);
            weights.setConditionWeight(0.4);
            weights.setId(1);
            return matchWeightsRepository.save(weights);
        });
    }

    @Override
    @Transactional
    public void updateWeights(double skillWeight, double conditionWeight) {
        MatchWeights weights = matchWeightsRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Weights config not found"));

        weights.setSkillWeight(skillWeight);
        weights.setConditionWeight(conditionWeight);

        matchWeightsRepository.save(weights);
    }
}
