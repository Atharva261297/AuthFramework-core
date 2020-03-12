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
@Table(name = "creds", schema = "user_data")
public class HashModel {

    @Id
    private String id;
    @Column
    private byte[] pass;

}
