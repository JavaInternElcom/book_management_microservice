package com.elcom.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private String uuid;
    private String name;
    private String authorId;
    private String categoryId;

}
