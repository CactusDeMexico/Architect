package com.pancarte.architecte.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "meeting", schema = "public")
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_meeting")
    private int id;
    @Column(name = "invitationSended")
    private boolean invitationSended;

    @Column(name = "meetingValidate")
    private boolean meetingValidate;

    @Column(name = "date_sended")
    private Timestamp dateSended;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "closed")
    private boolean closed;
    @Column(name = "date_meeting")
    private Timestamp dateMeeting;
}
