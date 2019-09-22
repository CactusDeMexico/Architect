package com.pancarte.architecte.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * BlockedMail classe representant les emails bloqué et leurs causes
 * @author Marjorie Pancarte
 * @version 1.0
 */
@Entity
@Table(name = "blocked_mail", schema = "public")
@Getter
@Setter
public class BlockedMail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_blocked_mail")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "cause")
    private String cause;
}
