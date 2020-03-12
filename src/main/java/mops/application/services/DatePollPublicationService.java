package mops.application.services;

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
public class DatePollPublicationService {

    public static final String LINK_ALREADY_TAKEN = "Link already taken";
    private DatePollRepository datePollRepository;
    private GroupRepository groupRepository;

    /**
     * Beendet den Erstellungsprozess und speichert die erstellte Terminfindung.
     *
     * @param datePollBuilderAndView Um an den datePollConfigDto
     * @return DatePoll Objekt.
     */
    public DatePoll publishDatePoll(final DatePollBuilderAndView datePollBuilderAndView) {
        DatePoll created = datePollBuilderAndView.startBuildingDatePoll();
        datePollRepository.save(created);
        return created;
    }

    /**
     * Wenn der Link noch nicht vergeben wurde wird dieser als Veröffentlichungslink gesetzt.
     *
     * @param datePollBuilderAndView Builder vom DatePollCreateService
     * @param link                   link zur späteren Veröffentlichung
     */
    public void requestLink(final DatePollBuilderAndView datePollBuilderAndView, final DatePollLink link)
            throws IllegalArgumentException {
        datePollRepository.load(link).ifPresent(datePoll -> {
            throw new IllegalArgumentException(LINK_ALREADY_TAKEN);
        });
        DatePollBuilder builder = datePollBuilderAndView.getBuilder();
        datePollBuilderAndView.setValidation(
                builder.datePollLink(link).getValidationState()
        );
    }

    /**
     * Gibt die DatePoll zurück, die nun die Information enthält, dass sie für die User einer
     * bestimmten Gruppe bestimmt ist.
     *
     * @param datePollBuilderAndView Builder vom DatePollCreateService
     * @param groupID                Id der betreffenden Gruppe
     */
    public void forGroup(final DatePollBuilderAndView datePollBuilderAndView, final GroupId groupID) {
        List<UserId> userList = groupRepository.getUsersFromGroupByGroupId(groupID);
        forCertainUsers(datePollBuilderAndView, userList);
    }

    /**
     * Gibt die DatePoll zurück, die nun die Information enthält, dass sie für die übergebenen User
     * bestimmt ist.
     *
     * @param datePollBuilderAndView Builder vom DatePollCreateService
     * @param participants           Liste der betreffenden User
     */
    public void forCertainUsers(final DatePollBuilderAndView datePollBuilderAndView, final List<UserId> participants) {
        DatePollBuilder builder = datePollBuilderAndView.getBuilder();
        datePollBuilderAndView.setValidation(
                builder.participants(participants).getValidationState()
        );
    }

}