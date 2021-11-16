package com.academy.key.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HolidayRequestDTO {

    private String processInstanceKey;

    private String employeeName;

    private int numberOfHolidays;

    private String description;

    private String phoneNumber;

}
