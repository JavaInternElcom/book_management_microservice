package com.demo.grpc.service.model;

import com.demo.grpc.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    @Id
    private Long id;

    private String username;

    private String password;

    public static UserEntity fromProto(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        return userEntity;
    }

    public User toProto() {
        return User.newBuilder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .build();
    }

}
