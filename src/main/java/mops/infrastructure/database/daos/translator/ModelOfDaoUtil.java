package mops.infrastructure.database.daos.translator;

import mops.domain.models.PollLink;
import mops.domain.models.Timespan;
import mops.domain.models.datepoll.DatePoll;
import mops.domain.models.datepoll.DatePollBallot;
import mops.domain.models.datepoll.DatePollConfig;
import mops.domain.models.datepoll.DatePollEntry;
import mops.domain.models.datepoll.DatePollMetaInf;
import mops.domain.models.datepoll.DatePollRecordAndStatus;
import mops.domain.models.pollstatus.PollRecordAndStatus;
import mops.domain.models.questionpoll.QuestionPoll;
import mops.domain.models.questionpoll.QuestionPollBallot;
import mops.domain.models.questionpoll.QuestionPollConfig;
import mops.domain.models.questionpoll.QuestionPollEntry;
import mops.domain.models.questionpoll.QuestionPollMetaInf;
import mops.domain.models.user.User;
import mops.domain.models.user.UserId;
import mops.infrastructure.database.daos.TimespanDao;
import mops.infrastructure.database.daos.PollRecordAndStatusDao;
import mops.infrastructure.database.daos.UserDao;
import mops.infrastructure.database.daos.datepoll.DatePollConfigDao;
import mops.infrastructure.database.daos.datepoll.DatePollDao;
import mops.infrastructure.database.daos.datepoll.DatePollEntryDao;
import mops.infrastructure.database.daos.datepoll.DatePollMetaInfDao;
import mops.infrastructure.database.daos.questionpoll.QuestionPollConfigDao;
import mops.infrastructure.database.daos.questionpoll.QuestionPollDao;
import mops.infrastructure.database.daos.questionpoll.QuestionPollEntryDao;
import mops.infrastructure.database.daos.questionpoll.QuestionPollMetaInfDao;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"PMD.TooManyMethods", "PMD.ExcessiveImports", "PMD.LawOfDemeter",
    "PMD.DataflowAnomalyAnalysis"})
public final class ModelOfDaoUtil {
    public static DatePoll pollOf(DatePollDao pollDao) {
        final PollRecordAndStatus pollRecordAndStatus =
                ModelOfDaoUtil.pollRecordAndStatusOf(pollDao.getPollRecordAndStatusDao());

        final DatePollMetaInf metaInf =
                ModelOfDaoUtil.metaInfOf(pollDao.getMetaInfDao());

        final User creator = ModelOfDaoUtil.userOf(pollDao.getCreatorUserDao());

        final DatePollConfig config =
                ModelOfDaoUtil.configOf(pollDao.getConfigDao());

        final Set<DatePollEntry> entries = extractDatePollEntries(pollDao.getEntryDaos());

        final Set<User> participants = extractUser(pollDao.getUserDaos());
        final Set<UserId> participantIds = extractIds(participants);

        //TODO: add (same for Datepoll and Questionpoll ???)
        final Set<DatePollBallot> ballots = null;

        final PollLink newLink = ModelOfDaoUtil.linkOf(pollDao.getLink());

        return new DatePoll(
                (DatePollRecordAndStatus) pollRecordAndStatus,
                metaInf,
                creator.getId(),
                config,
                entries,
                participantIds,
                ballots,
                newLink
        );
    }
    public static QuestionPoll pollOf(QuestionPollDao pollDao) {
        final PollRecordAndStatus pollRecordAndStatus =
                ModelOfDaoUtil.pollRecordAndStatusOf(pollDao.getPollRecordAndStatusDao());

        final QuestionPollMetaInf metaInf =
                ModelOfDaoUtil.metaInfOf(pollDao.getMetaInfDao());

        final User creator = ModelOfDaoUtil.userOf(pollDao.getCreatorUserDao());

        final QuestionPollConfig config =
                ModelOfDaoUtil.configOf(pollDao.getConfigDao());

        final Set<QuestionPollEntry> entries = extractQuestionPollEntries(pollDao.getEntryDaos());

        final Set<User> participants = extractUser(pollDao.getUserDaos());
        final Set<UserId> participantIds = extractIds(participants);

        //TODO: add (same for Datepoll and Questionpoll ???)
        final Set<QuestionPollBallot> ballots = null;

        final PollLink newLink = ModelOfDaoUtil.linkOf(pollDao.getLink());

        return new QuestionPoll(
                pollRecordAndStatus,
                metaInf,
                creator.getId(),
                config,
                entries,
                participantIds,
                ballots,
                newLink
        );
    }

    public static PollRecordAndStatus pollRecordAndStatusOf(PollRecordAndStatusDao dao) {
        final PollRecordAndStatus pollRecordAndStatus = new PollRecordAndStatus();
        pollRecordAndStatus.setLastModified(dao.getLastmodified());
        return pollRecordAndStatus;
    }

    public static DatePollMetaInf metaInfOf(DatePollMetaInfDao dao) {
        final TimespanDao timespanDao = dao.getTimespan();
        return new DatePollMetaInf(
                dao.getTitle(),
                dao.getDescription(),
                dao.getLocation(),
                ModelOfDaoUtil.timespanOf(timespanDao)
        );
    }

    public static QuestionPollMetaInf metaInfOf(QuestionPollMetaInfDao dao) {
        final TimespanDao timespanDao = dao.getTimespan();
        return new QuestionPollMetaInf(
                dao.getTitle(),
                dao.getQuestion(),
                dao.getDescription(),
                ModelOfDaoUtil.timespanOf(timespanDao)
        );
    }

    public static User userOf(UserDao dao) {
        final UserId userId = new UserId(Long.toString(dao.getId()));
        return new User(userId);
    }

    public static DatePollConfig configOf(DatePollConfigDao dao) {
        return new DatePollConfig(
                dao.isVoteIsEditable(),
                dao.isOpenForOwnEntries(),
                dao.isSingleChoice(),
                dao.isPriorityChoice(),
                dao.isAnonymous(),
                dao.isOpen()
        );
    }

    public static QuestionPollConfig configOf(QuestionPollConfigDao dao) {
        return new QuestionPollConfig(
                dao.isSingleChoice(),
                dao.isAnonymous(),
                dao.isOpen()
        );
    }

    private static Set<DatePollEntry> extractDatePollEntries(Set<DatePollEntryDao> daoEntries) {
        final Set<DatePollEntry> datePollEntries = new HashSet<>();
        for (final DatePollEntryDao daoEntry : daoEntries) {
            final DatePollEntry currentEntry = ModelOfDaoUtil.entryOf(daoEntry);
            datePollEntries.add(currentEntry);
        }
        return datePollEntries;
    }

    private static Set<QuestionPollEntry> extractQuestionPollEntries(Set<QuestionPollEntryDao> daoEntries) {
        final Set<QuestionPollEntry> questionPollEntries = new HashSet<>();
        for (final QuestionPollEntryDao daoEntry : daoEntries) {
            final QuestionPollEntry currentEntry = ModelOfDaoUtil.entryOf(daoEntry);
            questionPollEntries.add(currentEntry);
        }
        return questionPollEntries;
    }

    public static DatePollEntry entryOf(DatePollEntryDao dao) {
        final Timespan entryTimespan = timespanOf(dao.getTimespanDao());
        //TODO: add votes
        return new DatePollEntry(entryTimespan);
    }

    public static QuestionPollEntry entryOf(QuestionPollEntryDao dao) {
        return new QuestionPollEntry(dao.getEntryName());
    }

    public static Timespan timespanOf(TimespanDao dao) {
        return new Timespan(dao.getStartDate(), dao.getEndDate());
    }

    private static Set<User> extractUser(Set<UserDao> daoUser) {
        final Set<User> user = new HashSet<>();
        for (final UserDao dao : daoUser) {
            final User currentUser = ModelOfDaoUtil.userOf(dao);
            user.add(currentUser);
        }
        return user;
    }

    private static Set<UserId> extractIds(Set<User> user) {
        final Set<UserId> userIds = new HashSet<>();
        for (final User u : user) {
            final UserId id = u.getId();
            userIds.add(id);
        }
        return userIds;
    }

    public static PollLink linkOf(String link) {
        return new PollLink(UUID.fromString(link));
    }

    /**
     * Wird nie instanziiert da utility Klasse.
     */
    private ModelOfDaoUtil() {
        return;
    }

}