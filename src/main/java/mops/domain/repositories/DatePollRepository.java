package mops.domain.repositories;

import mops.domain.models.datepoll.DatePoll;
import mops.domain.models.datepoll.DatePollLink;
import mops.domain.models.user.UserId;

import java.util.Optional;
import java.util.Set;

/**
 * Repository interface für datePolls.
 */
public interface DatePollRepository {
    /**
     * Methodenkopf für die DatePoll-Lademethode.
     * @param link Eindeutig identifizierender link einer Terminfindung.
     * @return DatePoll;
     */
    Optional<DatePoll> load(DatePollLink link);

    /**
     * Methodenkopf für die DatePoll-Speichermethode.
     * @param datePoll Zu speichernde DatePoll
     */
    void save(DatePoll datePoll);

    Set<DatePoll> getDatePollsByUserId(UserId userId);
    DatePoll getDatePollByLink(DatePollLink datePollLink);
}
