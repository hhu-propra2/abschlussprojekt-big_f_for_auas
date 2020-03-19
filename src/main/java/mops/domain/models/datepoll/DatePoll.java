package mops.domain.models.datepoll;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import mops.domain.models.pollstatus.PollStatus;
import mops.domain.models.user.User;
import mops.domain.models.user.UserId;

import java.util.List;

@AllArgsConstructor
@SuppressFBWarnings(
        value = "URF_UNREAD_FIELD",
        justification = "Implemntierung folgt")
public final class DatePoll {

    @Getter
    private DatePollRecordAndStatus datePollRecordAndStatus;
    @Getter
    private DatePollMetaInf datePollMetaInf;
    private final UserId creator;
    private DatePollConfig datePollConfig;
    private Set<DatePollEntry> datePollEntries;
    private Set<UserId> participants;
    private Set<DatePollBallot> datePollBallots;

    @Getter
    private DatePollLink datePollLink;

    public static DatePollBuilder builder() {
        return new DatePollBuilder();
    }

    public PollStatus getUserStatus(User user) {
        return datePollRecordAndStatus.getUserStatus(user);
    }

    public void castBallot(DatePollBallot ballot) {
        updatePollStatus();
        if (pollRecordAndStatus.isTerminated()) {
            return;
        }
        if (!datePollConfig.isOpen() && !participants.contains(ballot.getUser())) {
            return;
        }
        if (datePollConfig.isSingleChoice() && ballot.getSelectedEntriesYes().size() > 1) {
            return;
        }
    }

    private void updatePollStatus() {
        if (datePollMetaInf.isBeforeEnd(LocalDateTime.now())) {
            pollRecordAndStatus.terminate();
        }
    }
}
