package mops.domain.models.datepoll;

import lombok.RequiredArgsConstructor;
import mops.domain.models.pollstatus.PollRecordAndStatus;

/**
 * Um die Kopplung zwischen Aggregaten und Packages aufrecht zu erhalten, wird hier einfach alles
 * aus PollRecordAndStatus geerbt.
 */
@RequiredArgsConstructor
public class DatePollRecordAndStatus extends PollRecordAndStatus {

    /**
     * Beendet den Datepoll.
     */
    void terminate() { //NOPMD
        super.terminatePoll();
    }
}
