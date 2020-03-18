package mops.application.services;

import lombok.NoArgsConstructor;
import mops.domain.models.datepoll.DatePoll;
import mops.domain.models.datepoll.DatePollBuilder;
import mops.domain.models.datepoll.DatePollLink;
import mops.domain.models.group.GroupId;
import mops.domain.models.user.UserId;
import mops.domain.repositories.DatePollRepository;
import mops.domain.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@NoArgsConstructor // PMD zuliebe
public class DatePollSyndicationService {

    public static final String LINK_ALREADY_TAKEN = "Link already taken";
    private transient DatePollRepository datePollRepository;
    private transient GroupRepository groupRepository;

    /**
     * Beendet den Erstellungsprozess und speichert die erstellte Terminfindung.
     *
     * @param builder Um an den datePollConfigDto
     * @return DatePoll Objekt.
     */
    public DatePoll publishDatePoll(final DatePollBuilder builder) {
        final DatePoll created = builder.build();
        datePollRepository.save(created);
        return created;
    }

    /**
     * Wenn der Link noch nicht vergeben wurde wird dieser als Veröffentlichungslink gesetzt.
     *
     * @param builder Builder für DatePoll
     * @param link    link zur späteren Veröffentlichung
     */
    @SuppressWarnings({"PMD.LawOfDemeter"})
    /* Optional check stellt keine wirkliche Verletzungs des Prinzip von LawOfDemeter da*/
    public void requestLink(final DatePollBuilder builder, final DatePollLink link) {
        datePollRepository.load(link).ifPresent(datePoll -> {
            throw new IllegalArgumentException(LINK_ALREADY_TAKEN);
        });
        builder.datePollLink(link);
    }

    /**
     * fügt zum übergebenen Builder die Information hinzuz, sodass sie für die User einer
     * bestimmten Gruppe bestimmt ist.
     *
     * @param builder Builder für DatePoll
     * @param groupID Id der betreffenden Gruppe
     */
    public void forGroup(final DatePollBuilder builder, final GroupId groupID) {
        final List<UserId> userList = groupRepository.getUsersFromGroupByGroupId(groupID);
        forCertainUsers(builder, userList);
    }

    /**
     * fügt zum übergebenen Builder die Information hinzuz, sodass er nun alle übergebenen User als
     * Teilnehmer enthält.
     *
     * @param builder      Builder für DatePoll
     * @param participants Liste der betreffenden User
     */
    public void forCertainUsers(final DatePollBuilder builder, final List<UserId> participants) {
        builder.participants(participants);
    }

    /**
     * fügt zum übergebenen Builder die Information hinzuz, sodass er nun den übergebenen User als
     * Teilnehmer enthält.
     *
     * @param builder     Builder für DatePoll
     * @param participant Liste der betreffenden User
     */
    public void forCertainUser(final DatePollBuilder builder, final UserId participant) {
        forCertainUsers(builder, List.of(participant));
    }

}