package mops.domain.models.datepoll;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.With;
import mops.domain.models.ValidateAble;
import mops.domain.models.Validation;

@Value
@With
@AllArgsConstructor
public class DatePollConfig implements ValidateAble {

    /**
     * true: User kann selbst Termine zur Abstimmung hinzufügen.
     * false: User kann keine Termine zur Abstimmung hinzufügen.
     */
    private boolean openForOwnEntries;
    /**
     * true: Jeder, der abstimmt kann nur einen Termin auswählen.
     * false: Jeder, der abstimmt kann mehrere Termine auswählen.
     */
    private boolean singleChoice;
    /**
     * true: Man kann für die Auswahl der Termine nach einem Ampelystem Prioritäten angeben.
     * false: Man kann für jeden Termin nur 'ich kann' oder 'ich kann nicht' angeben.
     */
    private boolean priorityChoice;
    /**
     * true: Es ist nicht einsehbar, wer für welche Termine gestimmt hat.
     * false: Es ist einsehbar, wer für welche Termine gestimmt hat.
     */
    private boolean anonymous;
    /**
     * true: Die DatePoll ist öffentlich; die Liste der Participants (siehe Klasse DatePoll) ist irrelevant.
     * false: Die DatePoll ist nur für User in der Liste der Participants (siehe Klasse DatePoll) bestimmt.
     */
    private boolean open;

    /**
     * NoArgsConstructor - Default Werte fuer die Konfiguration einer Terminfindung.
     */
    public DatePollConfig() {
        this.priorityChoice = false;
        this.anonymous = false;
        this.openForOwnEntries = false;
        this.open = false;
        this.singleChoice = false;
    }

    /**
     * ...
     * @return
     */
    @Override
    public Validation validate() {
        Validation validation = Validation.noErrors();
        return validation;
    }
}