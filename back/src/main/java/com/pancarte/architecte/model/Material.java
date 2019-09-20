package com.pancarte.architecte.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name ="material", schema = "public")
@Getter
@Setter
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_material")
    private int id;

    @Column(name = "name")
    private String name;
    @Column(name = "thickness")
    private int thickness;

    @Column(name = "opaque")
    private boolean opaque;


}
