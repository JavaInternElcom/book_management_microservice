package com.elcom.library.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.Collection;

@Document(indexName = "category")
@Entity
@Table(name = "category")
@Data
@Proxy(lazy = false)
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

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

    @Column(name = "code")
    @Field(type = FieldType.Text)
    private String code;

    // mark property is ignored
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.REMOVE)
    private Collection<Book> books;
}