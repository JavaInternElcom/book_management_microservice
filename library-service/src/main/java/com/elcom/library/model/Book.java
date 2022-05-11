package com.elcom.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Document(indexName = "book")
@Entity
@Table(name = "book")
@Data
@Proxy(lazy = false)
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    @Size(min = 1, max = 40)
    @Column(name = "uuid")
    @Field(type = FieldType.Keyword)
    private String uuid;

    @Column(name = "name")
    @Field(type = FieldType.Text)
    private String name;

    @Column(name = "first_letter")
    @Field(type = FieldType.Text)
    private String firstLetter;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}