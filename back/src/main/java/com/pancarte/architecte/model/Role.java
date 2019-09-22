package com.pancarte.architecte.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/**
 * classe representant un role
 * @author Marjorie Pancarte
 * @version 1.0
 */
@Entity
@Table(name ="role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_role")
    private int id;

    @Column(name = "role")
    private String role;



}
