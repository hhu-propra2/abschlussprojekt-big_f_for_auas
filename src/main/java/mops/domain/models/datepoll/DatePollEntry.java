package mops.domain.models.datepoll;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mops.controllers.dtos.DatePollEntryDto;
import mops.domain.models.Timespan;
import mops.domain.models.ValidateAble;
import mops.domain.models.Validation;

import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public final class DatePollEntry implements ValidateAble {

    private final Timespan suggestedPeriod;
    //Anzahl der Stimmen fuer diesen Termin.
    private int yesVotes;
    private int maybeVotes;

    @SuppressWarnings({"PMD.LawOfDemeter"})
    @Override
    /* noErrors() ist Konstruktor*/
    public Validation validate() {
        final Validation validation = Validation.noErrors();
        return validation.appendValidation(suggestedPeriod.validate());
    }

    /**
     * Vergleich von Entries an Hand des vorgeschlagenen Timeslots.
     *
     * @param other
     * @return boolean
     */
    boolean representsSamePeriod(DatePollEntry other) { //NOPMD
        return suggestedPeriod.equals(other.suggestedPeriod);
    }

    void incYesVote() { //NOPMD
        yesVotes++;
    }

    void decYesVote() { //NOPMD
        yesVotes--;
    }

    void incMaybeVote() { //NOPMD
        yesVotes++;
    }

    void decMaybeVote() { //NOPMD
        maybeVotes--;
    }

    /**
     * Gibt SetA - SetB zurück.
     * Bestimmt Gruppenzugehörigkeit nur an Hand der Timespan Objekten in DatePollEntry.
     *
     * @param setA
     * @param setB
     * @return SetA - SetB (alle Elemente aus A, welche nicht in B sind)
     */
    // lawOfDemeter liegt an der stream notation. gleicher code in loop form erzeugt kein warning.
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DefaultPackage"})
    static Set<DatePollEntry> difference(Set<DatePollEntry> setA, Set<DatePollEntry> setB) { //NOPMD
        return setA.stream().filter(entryFromSetA -> setB.stream()
                .noneMatch(entryFromSetB -> entryFromSetB.representsSamePeriod(entryFromSetA)))
                .collect(Collectors.toSet());
    }

    public DatePollEntryDto toDto() {
        return new DatePollEntryDto(this.suggestedPeriod.getStartDateTime(),
                this.suggestedPeriod.getEndDateTime());
    }
}
