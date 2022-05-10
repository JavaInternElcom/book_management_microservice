package com.elcom.id.service.impl;

import com.elcom.id.messaging.rabbitmq.model.Student;
import com.elcom.id.repository.StudentRepository;
import com.elcom.id.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void delete(String uuid) {
        studentRepository.deleteById(uuid);
    }

    @Override
    public Student findStudentByUuid(String uuid) {
        Optional<Student> student = studentRepository.findById(uuid);
        if(student.isPresent()){
            return student.get();
        }
        return null;
    }
}
