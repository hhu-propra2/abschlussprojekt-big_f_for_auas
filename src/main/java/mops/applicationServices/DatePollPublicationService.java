package mops.applicationServices;

import mops.database.DatePollRepository;
import mops.database.GroupRepository;
import mops.domain.models.DatePoll.DatePoll;
import mops.domain.models.Group.GroupId;
import org.springframework.stereotype.Service;


@Service
public class DatePollPublicationService {
    /**
     * DatePollRepository mit den bisher gespeicherten DatePolls.
     */
    private DatePollRepository datePollRepository;
    /**
     * GroupRepository mit den bisher gespeicherten Groups.
     */
    private GroupRepository groupRepository;

    /**
     *
     * @param datePollBuilderAndView Um an den datePollConfigDto
     * @return
     */
    public DatePoll publicationByLink(final DatePollBuilderAndView datePollBuilderAndView) {

        return datePollBuilderAndView.startBuildingDatePoll();
    }

    /**
     * Gibt die DatePoll zurück, die nun die Information enthält, dass sie für die User einer
     * bestimmten Gruppe bestimmt ist.
     * @param datePollID Id der betreffenden datePoll
     * @param groupID Id der betreffenden Gruppe
     * @return DatePoll
     */
    public DatePoll publicationForGroup(final DatePollID datePollID, final GroupId groupID) {
        DatePoll datePoll = datePollRepository.getDatePollById(datePollID);
        datePoll.addListOfUsersToParticipants(groupRepository.getUsersFromGroupByGroupId(groupID));
        return datePoll;
    }

    /**
     * Gibt die DatePoll zurück, die nun die Information enthält, dass sie für die übergebenen User
     * bestimmt ist.
     * @param datePollID Id der betreffenden datePoll
     * @param participants Liste der betreffenden User
     * @return DatePoll
     */
    public DatePoll publicationForCertainUsers(final DatePollID datePollID, final List<User> participants) {
        DatePoll datePoll = datePollRepository.getDatePollById(datePollID);
        datePoll.addListOfUsersToParticipants(participants);
        return datePoll;
    }
}
