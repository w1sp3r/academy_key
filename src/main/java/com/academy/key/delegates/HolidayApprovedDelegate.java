package com.academy.key.delegates;

import com.academy.key.DTOs.MessageDTO;
import com.academy.key.services.TwilioService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("holidayApprovedDelegate")
public class HolidayApprovedDelegate implements JavaDelegate {

    private TwilioService twilioService;

    @Autowired
    public HolidayApprovedDelegate(TwilioService twilioService){
        this.twilioService = twilioService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {

        String APPROVE_MSG = "Hello %s! Your vacation has been approved!";
        final String employeeName = delegateExecution.getVariable("employee").toString();

        log.info("======= sending approved approval SMS to the {} {}",
                employeeName, delegateExecution.getVariable("phoneNumber").toString());

        MessageDTO messageDTO = twilioService
                .sendSmsTo(delegateExecution.getVariable("phoneNumber").toString(),
                        String.format(APPROVE_MSG, employeeName));

        log.info("======= twilio response on sending SMS message {} =======", messageDTO);
    }

}
