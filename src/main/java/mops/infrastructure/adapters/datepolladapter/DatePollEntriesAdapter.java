package mops.infrastructure.adapters.datepolladapter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import mops.application.services.DatePollVoteService;
import mops.application.services.PollInfoService;
import mops.infrastructure.controllers.dtos.DatePollEntryDto;
import mops.infrastructure.controllers.dtos.DatePollUserEntryOverview;
import mops.infrastructure.controllers.dtos.FormattedDatePollEntryDto;
import mops.domain.models.datepoll.DatePollBallot;
import mops.domain.models.datepoll.DatePollEntry;
import mops.domain.models.PollLink;
import mops.domain.models.user.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.LawOfDemeter", "checkstyle:DesignForExtension"})
/* keine Zeit sich um bessere Kapslung zu kümmern */
@Service
public class DatePollEntriesAdapter {

    private final transient DatePollVoteService voteService;
    private final transient PollInfoService infoService;

    @Autowired
    public DatePollEntriesAdapter(DatePollVoteService voteService, PollInfoService infoService) {
        this.voteService = voteService;
        this.infoService = infoService;
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public DatePollUserEntryOverview showUserEntryOverview(PollLink link, UserId user) {
        final DatePollUserEntryOverview result = new DatePollUserEntryOverview();
        DatePollBallot ballot;
        try {
            ballot = voteService.showUserVotes(user, link);
        } catch (NoSuchElementException e) {
            ballot = new DatePollBallot(user);
        }
        final List<DatePollEntryDto> allEntries = infoService.getEntries(link).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        final List<DatePollEntryDto> votedForYesEntries = ballot.getSelectedEntriesYes()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        final List<DatePollEntryDto> votedForMaybeEntries = ballot.getSelectedEntriesMaybe()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());

        allEntries.sort(DatePollEntryDto::compareTo);
        votedForYesEntries.sort(DatePollEntryDto::compareTo);
        votedForMaybeEntries.sort(DatePollEntryDto::compareTo);

        result.setAllEntries(allEntries);
        result.setVotedYes(votedForYesEntries);
        result.setVotedMaybe(votedForMaybeEntries);
        return result;
    }

    public Set<DatePollEntryDto> showAllEntries(PollLink link) {
        final Set<DatePollEntry> entries = infoService.getEntries(link);
        return entries.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<FormattedDatePollEntryDto> getAllEntriesFormatted(PollLink link) {
        final Set<DatePollEntry> entries = infoService.getEntries(link);
        return entries.stream()
                .map(this::toFormattedDTO)
                .collect(Collectors.toSet());
    }

    private DatePollEntryDto toDTO(DatePollEntry entry) {
        return new DatePollEntryDto(
                entry.getSuggestedPeriod().getStartDate(),
                entry.getSuggestedPeriod().getEndDate()
        );
    }

    private FormattedDatePollEntryDto toFormattedDTO(DatePollEntry entry) {
        final String formatted = entry.getSuggestedPeriod().toString();
        return new FormattedDatePollEntryDto(formatted);
    }


    /** parsed die Overview in domain models und ruft den service auf.
     *
     * @param link
     * @param user
     * @param overview
     */
    public void vote(PollLink link, UserId user, DatePollUserEntryOverview overview) {
        if (overview.isVotedMaybeNull()) {
            overview.setVotedMaybe(new ArrayList<>());
        }
        voteService.vote(link, user,
                overview.getVotedYes().stream()
                        .map(this::toEntryDomain)
                        .collect(Collectors.toSet()),
                overview.getVotedMaybe().stream()
                        .map(this::toEntryDomain)
                        .collect(Collectors.toSet()));
    }

    private DatePollEntry toEntryDomain(DatePollEntryDto entryDto) {
        return new DatePollEntry(entryDto.getTimespan());
    }
}
