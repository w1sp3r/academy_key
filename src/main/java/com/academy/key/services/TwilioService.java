package com.academy.key.services;

import com.academy.key.DTOs.MessageDTO;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.Twilio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TwilioService {

    // region twilio account details
    private static final String ACCOUNT_SID = "YOUR ACCOUNT SID";

    private static final String AUTH_TOKEN = "YOUR ACCOUNT AUTH";

    private static final String NUMBER = "A PHONE NUMBER I TWILIO";
    // endregion

    @Transactional
    public MessageDTO sendSmsTo(String toNumber, String message) {

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message approveMessage = Message.creator(
                new PhoneNumber(toNumber),          // to
                new PhoneNumber(NUMBER),            // from
                message                             // body
        ).create();

        return MessageDTO.builder()
                .messageSid(approveMessage.getSid())
                .body(approveMessage.getBody())
                .from(approveMessage.getFrom().toString())
                .to(approveMessage.getTo())
                .createdDate(approveMessage.getDateCreated().toLocalDateTime())
                .build();

    }

}
