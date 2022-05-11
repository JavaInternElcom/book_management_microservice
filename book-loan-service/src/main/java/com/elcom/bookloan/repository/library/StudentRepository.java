package com.elcom.bookloan.repository.library;

import com.elcom.bookloan.model.library.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
}
