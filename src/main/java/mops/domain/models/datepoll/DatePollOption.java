package mops.domain.models.datepoll;

import mops.controllers.DatePollOptionDto;

import java.util.Date;

class DatePollOption {
    private Date date;
    //Anzahl der Stimmen fuer diesen Termin.
    private int votes;

    DatePollOption(final DatePollOptionDto optionDto) {

    }
}
