package com.elcom.bookloan.service.library.impl;

import com.elcom.bookloan.model.library.Student;
import com.elcom.bookloan.repository.library.StudentRepository;
import com.elcom.bookloan.service.library.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student findByUuid(String uuid) {
        Optional<Student> student = studentRepository.findById(uuid);
        if(student.isPresent())
            return student.get();
        return null;
    }
}
