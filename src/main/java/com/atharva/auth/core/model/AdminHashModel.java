package com.atharva.auth.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "creds_admin", schema = "auth_data")
public class AdminHashModel {

    @Id
    private String id;
    @Column
    private byte[] pass;

    public AdminHashModel(HashModel hash) {
        this.id = hash.getId();
        this.pass = hash.getPass();
    }
}
