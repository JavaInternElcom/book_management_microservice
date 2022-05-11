package com.elcom.bookloan.service.library;

import com.elcom.bookloan.model.library.Book;

public interface BookService {

    Book findByUuid(String uuid);
}
