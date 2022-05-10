package com.elcom.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Collection;

@Document(indexName = "author")
@Entity
@Table(name = "author")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @Size(min = 1, max = 40)
    @Column(name = "uuid")
    @Field(type = FieldType.Keyword)
    private String uuid;

    @Column(name = "last_name")
    @Field(type = FieldType.Text)
    private String lastName;

    @Column(name = "first_name")
    @Field(type = FieldType.Text)
    private String firstName;

    @Column(name = "phone_number")
    @Field(type = FieldType.Text)
    private String phoneNumber;

    @Column(name = "email")
    @Field(type = FieldType.Text)
    private String email;

    @Column(name = "address")
    @Field(type = FieldType.Text)
    private String address;

    // mark property is ignored
    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private Collection<Book> book;
}