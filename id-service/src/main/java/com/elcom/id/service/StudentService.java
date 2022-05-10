package com.elcom.id.service;

import com.elcom.id.model.Student;

import java.util.List;

public interface StudentService {

    void save(Student student);

    List<Student> getAllStudents();

    void delete(String uuid);

    Student findStudentByUuid(String uuid);
}
