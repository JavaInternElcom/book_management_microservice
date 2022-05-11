package com.elcom.bookloan.service.library;

import com.elcom.bookloan.model.library.Student;

public interface StudentService {

    Student findByUuid(String uuid);
}
