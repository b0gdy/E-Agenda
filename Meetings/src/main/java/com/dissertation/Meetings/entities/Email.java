package com.dissertation.Meetings.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String recipient;
    private String subject;
    private String body;
//    private String attachment;

//    private String meetingId;

}
