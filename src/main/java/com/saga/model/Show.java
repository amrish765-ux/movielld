package com.saga.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.xml.crypto.Data;
import java.util.Date;

@AllArgsConstructor
@Getter
public class Show {
    private final String id;
    private final Movie movie;
    private final Screen screen;
    private final Date startTime;
    private final Integer durationInSeconds;


}
