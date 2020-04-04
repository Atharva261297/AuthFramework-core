package com.atharva.auth.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserId.class)
@Table(name = "creds_user", schema = "auth_data")
public class UserHashModel {

    @Id
    private String id;

    @Id
    private String projectId;

    @Column
    private byte[] pass;

    public UserHashModel(HashModel hash, String projectId) {
        this.id = hash.getId();
        this.pass = hash.getPass();
        this.projectId = projectId;
    }

}
