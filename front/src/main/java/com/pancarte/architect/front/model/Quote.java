package com.pancarte.architect.front.model;

import lombok.Getter;
import lombok.Setter;


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
