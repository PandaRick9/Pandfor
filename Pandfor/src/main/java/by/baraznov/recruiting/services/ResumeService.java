package by.baraznov.recruiting.services;

import by.baraznov.recruiting.dto.resumePage.ResumeDto;
import by.baraznov.recruiting.models.Resume;

import java.util.List;


public interface ResumeService {
    void save(Resume resume);
    Resume findOne(Integer id);
    List<Resume> findAll();
    void delete(Integer id);

}
