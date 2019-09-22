package com.pancarte.architecte.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
/**
 * classe representant un devis
 * @author Marjorie Pancarte
 * @version 1.0
 */
@Entity
@Table(name ="quote", schema = "public")
@Getter
@Setter
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_quote")
    private int id;

    @Column(name = "name")
    private String projectName;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "surface")
    private int surface;

    @Column(name = "email")
    private String email;
    @Column(name = "urlImg")
    private String urlImg;


}
