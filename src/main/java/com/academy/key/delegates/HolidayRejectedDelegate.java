package com.academy.key.delegates;

import com.academy.key.DTOs.MessageDTO;
import com.academy.key.services.TwilioService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("holidayRejectedDelegate")
public class HolidayRejectedDelegate implements JavaDelegate {

    public TwilioService twilioService;

    @Autowired
    public HolidayRejectedDelegate(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String REJECT_MSG = "Hello %s! Your vacation has been rejected. Try another range maybe?";
        final String employeeName = delegateExecution.getVariable("employee").toString();
        log.info("======= sending rejected approval SMS to the {}",
                employeeName);
        MessageDTO messageDTO = twilioService
                .sendSmsTo(delegateExecution.getVariable("phoneNumber").toString(),
                        String.format(REJECT_MSG, employeeName));

        log.info("======= twilio response on sending SMS message {} =======", messageDTO);
    }

}
