package by.baraznov.recruiting.services;

import by.baraznov.recruiting.models.Resume;

import java.util.List;


public interface ResumeService {
    void save(Resume resume);
    List<Resume> findAll();

}
