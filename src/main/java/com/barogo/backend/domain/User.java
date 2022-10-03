package com.barogo.backend.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "BAROGO_USER")
@Data
public class User implements Serializable {

    @Id
    private String userId;
    private String password;
    private String userName;
    private String email;
}
