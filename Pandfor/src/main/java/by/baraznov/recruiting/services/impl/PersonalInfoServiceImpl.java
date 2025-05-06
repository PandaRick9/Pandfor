package by.baraznov.recruiting.services.impl;

import by.baraznov.recruiting.models.PersonalInfo;
import by.baraznov.recruiting.repositories.PersonalInfoRepository;
import by.baraznov.recruiting.services.PersonalInfoService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PersonalInfoServiceImpl implements PersonalInfoService {
    private final PersonalInfoRepository personalInfoRepository;

    @Override
    @Transactional
    public void save(PersonalInfo personalInfo) {
        personalInfoRepository.save(personalInfo);
    }
}
