package com.pancarte.architect.front.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class Meeting {

    private int id;

    private boolean invitationSended;


    private boolean meetingValidate;


    private Timestamp dateSended;


    private String purpose;

    private boolean closed;

    private Timestamp dateMeeting;
}
