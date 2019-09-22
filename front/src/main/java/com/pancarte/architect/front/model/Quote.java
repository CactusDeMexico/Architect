package com.pancarte.architect.front.model;

import lombok.Getter;
import lombok.Setter;

/**
 * classe representant un devis
 * @author Marjorie Pancarte
 * @version 1.0
 */
@Getter
@Setter
public class Quote {

    private int id;


    private String projectName;


    private String description;


    private String type;


    private int surface;
    private String email;
    private String urlImg;
}
