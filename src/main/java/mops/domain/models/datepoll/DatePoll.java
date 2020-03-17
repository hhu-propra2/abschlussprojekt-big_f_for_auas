package mops.domain.models.datepoll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mops.domain.models.pollstatus.PollStatus;
import mops.domain.models.user.User;
import mops.domain.models.user.UserId;
import java.util.List;

@Getter
@AllArgsConstructor
public final class DatePoll {

    /**
     * Speichert Status der Umfrage, global und userspezifisch.
     */
    private PollRecordAndStatus pollRecordAndStatus;
    /**
     * Meta-Informationen zur DatePoll.
     */
    private DatePollMetaInf datePollMetaInf;
    /**
     * Creator (Typ User) der DatePoll.
     */
    private UserId creator;
    /**
     * Konfiguration der DatePoll. Aggregiert in einem eigens dafür angelegten Objekt.
     */
    private DatePollConfig datePollConfig;
    /**
     * Liste der Terminoptionen der DatePoll.
     */
    private List<DatePollOption> datePollOptions;
    /**
     * Liste der User, die an der Abstimmung teilnehmen **können**.
     */
    private List<UserId> participants;

    /**
     * Link fuer den DatePoll.
     */
    private final DatePollLink datePollLink;

    public static DatePollBuilder builder() {
        return new DatePollBuilder();
    }

    public PollStatus getUserStatus(User user) {
        return pollRecordAndStatus.getUserStatus(user);
    }

}
