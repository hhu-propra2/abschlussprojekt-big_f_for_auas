package mops.database;

import mops.MopsApplication;
import mops.config.H2DatabaseConfigForTests;
import mops.domain.models.PollLink;
import mops.domain.models.Timespan;
import mops.domain.models.datepoll.*;
import mops.domain.models.group.Group;
import mops.domain.models.group.GroupId;
import mops.domain.models.user.UserId;
import mops.domain.repositories.DomainGroupRepository;
import mops.infrastructure.database.daos.UserDao;
import mops.infrastructure.database.daos.datepoll.DatePollDao;
import mops.infrastructure.database.daos.datepoll.DatePollEntryDao;
import mops.infrastructure.database.daos.translator.DaoOfModelUtil;
import mops.infrastructure.database.repositories.interfaces.DatePollEntryJpaRepository;
import mops.infrastructure.database.repositories.interfaces.DatePollJpaRepository;
import mops.infrastructure.database.repositories.interfaces.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MopsApplication.class, H2DatabaseConfigForTests.class})
@Transactional
@SuppressWarnings({"PMD.LawOfDemeter", "PMD.AtLeastOneConstructor", "PMD.ExcessiveImports"})
public class DatabaseDatePollIntegrityTest {
    private transient DatePoll datePoll;

    @Autowired
    private transient DatePollJpaRepository datePollJpaRepository;
    @Autowired
    private transient UserJpaRepository userJpaRepository;
    @Autowired
    private transient DatePollEntryJpaRepository datePollEntryJpaRepository;

    @Autowired
    private transient DomainGroupRepository domainGroupRepository;


    @SuppressWarnings({"checkstyle:DesignForExtension", "checkstyle:MagicNumber"})
    @BeforeEach
    public void setupDatePollRepoTest() {
        final Timespan timespan = new Timespan(LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        final DatePollMetaInf datePollMetaInf = new DatePollMetaInf("TestDatePoll", "Testing", "Uni", timespan);
        final UserId creator = new UserId("1234");
        final DatePollConfig datePollConfig = new DatePollConfig();
        final PollLink datePollLink = new PollLink();

        final Set<UserId> participants = new HashSet<>();
        IntStream.range(0, 3).forEach(i -> participants.add(new UserId(Integer.toString(i))));

        final Group group = new Group(new GroupId("1"), participants);

        domainGroupRepository.save(group);

        final Set<DatePollEntry> pollEntries = new HashSet<>();
        IntStream.range(0, 3).forEach(i -> pollEntries.add(new DatePollEntry(
                new Timespan(LocalDateTime.now().plusDays(i), LocalDateTime.now().plusDays(10 + i))
        )));

        datePoll = new DatePollBuilder()
                .datePollMetaInf(datePollMetaInf)
                .creator(creator)
                .datePollConfig(datePollConfig)
                .datePollEntries(pollEntries)
                .participatingGroups(Set.of(group.getId()))
                .datePollLink(datePollLink)
                .build();
    }

    @Test
    public void saveOneDatePollDao() {
        final DatePollDao datePollDao = DaoOfModelUtil.pollDaoOf(datePoll, new GroupId("1"));
        final String link = datePollDao.getLink();
        datePollJpaRepository.save(datePollDao);
        final DatePollDao datepollFound = datePollJpaRepository.findDatePollDaoByLink(link);

        assertThat(datepollFound.getLink()).isEqualTo(datepollFound.getLink());
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    public void testUsersOfDatePollPresence() {
        final DatePollDao datePollDao = DaoOfModelUtil.pollDaoOf(datePoll);
        datePollJpaRepository.save(datePollDao);
        final Set<UserDao> userDaoSet = userJpaRepository.findByDatePollSetContains(datePollDao);

        assertThat(userDaoSet).hasSize(3);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    @Test
    public void testDatePollEntryPresence() {
        final DatePollDao datePollDao = DaoOfModelUtil.pollDaoOf(datePoll);
        datePollJpaRepository.save(datePollDao);
        final Set<DatePollEntryDao> datePollEntryDaoSet = datePollEntryJpaRepository.findByDatePoll(datePollDao);

        assertThat(datePollEntryDaoSet).hasSize(3);
    }

    @Test
    public void testVotesForDatePollEntryAreZero() {
        final DatePollDao datePollDao = DaoOfModelUtil.pollDaoOf(datePoll);
        datePollJpaRepository.save(datePollDao);
        final DatePollEntryDao datePollEntry = datePollDao.getEntryDaos().iterator().next();

        assertThat(datePollEntry.getUserVotesFor().size()).isEqualTo(0);
    }
}
