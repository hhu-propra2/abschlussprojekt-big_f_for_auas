package mops.domain.models.questionpoll;

import lombok.Getter;
import mops.domain.models.FieldErrorNames;
import mops.domain.models.ValidateAble;
import mops.domain.models.Validation;
import mops.infrastructure.controllers.dtos.QuestionPollEntryDto;
import mops.utils.DomainObjectCreationUtils;

import java.util.Set;
import java.util.stream.Collectors;


/**
 * Speichert eine Option über die in einem QuestionPoll abgestimmt werden kann und
 *  verfolgt wie oft für diese Option abgestimmt wurde.
 */
public class QuestionPollEntry implements ValidateAble {
    @Getter
    private final String title;
    private transient int yesVotes; //NOPMD

    //Vorläufige Werte
    private static final int MAX_LENGTH_TITLE = 40;

    public QuestionPollEntry(final String title) {
        this.title = DomainObjectCreationUtils.convertNullToEmptyAndTrim(title);
    }

    /** Validate the title ( possibles Answer from Question) if it empty or only whitespaces etc.
     * Validate count if it is negative.
     * @return die Validation
     */
    @Override
    public Validation validate() {
        Validation validator = Validation.noErrors();
        if (this.title.isEmpty()) {
            validator = validator.appendValidation(
                    new Validation(FieldErrorNames.QUESTION_POLL_ENTRY_TITLE_IS_EMPTY));
        }
        if (this.title.length() > MAX_LENGTH_TITLE) {
            validator = validator.appendValidation(
                    new Validation(FieldErrorNames.QUESTION_POLL_ENTRY_TITLE_IS_TOO_LONG));
        }
        return validator;
    }

    /**
     * gibt zurück, ob der QuestionPollEntry die gleiche Antwort repräsentiert.
     * @param other QuestionPollEntry zum Vergleichen
     * @return Boolean, ob es der "gleiche" Entry ist.
     */
    private boolean representsSameAnswer(QuestionPollEntry other) { //NOPMD
        return title.equals(other.title);
    }

    /**
     * inkrementiert die Anzahl der YesVotes.
     */
    void incYesVote() { //NOPMD
        yesVotes++;
    }

    /**
     * dekrementiert die Anzahl der YesVotes.
     */
    void decYesVote() { //NOPMD
        yesVotes--;
    }


    /**
     * Gibt SetA - SetB zurück.
     * Bestimmt Gruppenzugehörigkeit nur an Hand der Timespan Objekten in DatePollEntry.
     *
     * @param setA erstes Set.
     * @param setB zweites Set.
     * @return SetA - SetB (alle Elemente aus A, welche nicht in B sind)
     */
    // lawOfDemeter liegt an der stream notation. gleicher code in loop form erzeugt kein warning.
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DefaultPackage"})
    static Set<QuestionPollEntry> difference(Set<QuestionPollEntry> setA, Set<QuestionPollEntry> setB) { //NOPMD
        return setA.stream().filter(entryFromSetA -> setB.stream()
                .noneMatch(entryFromSetB -> entryFromSetB.representsSameAnswer(entryFromSetA)))
                .collect(Collectors.toSet());
    }

    /**
     * Macht aus einem QuestionPollEntry ein QuestionPollEntryDto.
     * @return das entsprechende Dto
     */
    public QuestionPollEntryDto toDto() {
        return new QuestionPollEntryDto(this.title);
    }
}
