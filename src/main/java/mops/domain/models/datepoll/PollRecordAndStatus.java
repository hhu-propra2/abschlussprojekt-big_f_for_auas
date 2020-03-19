package mops.domain.models.datepoll;

import lombok.RequiredArgsConstructor;

/**
 * Um die Kopplung zwischen Aggregaten und Packages aufrecht zu erhalten, wird hier einfach alles
 * aus PollRecordAndStatus geerbt.
 */
@RequiredArgsConstructor
class PollRecordAndStatus extends mops.domain.models.pollstatus.PollRecordAndStatus {

    void terminate() { /* default */
        super.terminatePoll();
    }
}
