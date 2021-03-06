package mops.domain.models.datepoll;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import mops.domain.models.FieldErrorNames;
import mops.domain.models.PollFields;
import mops.domain.models.PollLink;
import mops.domain.models.ValidateAble;
import mops.domain.models.Validation;
import mops.domain.models.group.GroupId;
import mops.domain.models.user.UserId;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.TooManyMethods"})
@Log4j2
public final class DatePollBuilder {

    public static final String COULD_NOT_CREATE = "The Builder contains errors and DatePoll could not be created";
    public static final String DATE_POLL_BUILD_FAILED = "DatePoll build failed";
    public static final String FAILED_TO_VALIDATE = "Failed to validate: ";
    public static final String SEPERATOR = "----------------------";
    public static final String FIELD_NOT_PRESENT = "Field not present: ";
    //muss nicht 1-1 im ByteCode bekannt sein.
    private transient DatePollMetaInf metaInfTarget;
    private transient UserId pollCreatorTarget;
    private transient DatePollConfig configTarget;
    private final transient Set<DatePollEntry> pollEntryTargets = new HashSet<>();
    private final transient Set<GroupId> participatingGroupsTargets = new HashSet<>();
    private transient PollLink linkTarget;
    @Getter
    private Validation validationState;
    private final transient EnumSet<PollFields> validatedFields = EnumSet.noneOf(PollFields.class);

    private static final EnumSet<PollFields> VALID_SET = EnumSet.of(
            PollFields.DATE_POLL_META_INF,
            PollFields.POLL_LINK,
            PollFields.DATE_POLL_CONFIG,
            PollFields.DATE_POLL_ENTRIES,
            PollFields.CREATOR,
            PollFields.PARTICIPATING_GROUPS
    );

    public DatePollBuilder() {
        validationState = Validation.noErrors();
    }

    /*
     * dataflowAnomaly sollte nicht auftreten,
     * LawOfDemeter violation ist akzeptabel, denn die Validierung muss auf dem validateable Objekt aufgerufen werden
     * und wir wollen die Validierungsauswerdung in einem Validierungsobjekt Kapseln, so haben die ValidierungsObkete
     * weniger Responsebilities
     */
    @SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.LawOfDemeter"})//NOPMD
    private <T extends ValidateAble> Optional<T> validationProcess(T validateAble, PollFields fields) {
        final Validation newValidation = validateAble.validate();
        validationState = validationState.removeErrors(fields);
        validationState = validationState.appendValidation(newValidation);
        Optional<T> result = Optional.empty();
        if (newValidation.hasNoErrors()) {
            result = Optional.of(validateAble);
        }
        return result;
    }

    /*
     * hier liegt keine LawOfDemeter violation vor.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.DataflowAnomalyAnalysis", "PMD.UnusedPrivateMethod"})
    private <T extends ValidateAble> void validationProcessAndValidationHandling(
            T validateAble, Consumer<T> applyToValidated, PollFields addToFieldsAfterSuccessfulValidation) {
        validationProcess(validateAble, addToFieldsAfterSuccessfulValidation).ifPresent(validated -> {
            applyToValidated.accept(validated);
            validatedFields.add(addToFieldsAfterSuccessfulValidation);
        });
    }

    /*
     * streams stellen keine LawOfDemeter violation dar
     */
    @SuppressWarnings({"PMD.LawOfDemeter"})
    private <T extends ValidateAble> Set<T> validateAllAndGetCorrect(Set<T> mappedOptions, PollFields fields) {
        return mappedOptions.stream()
                .map((T validateAble) -> validationProcess(validateAble, fields))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    /**
     * Setzt die Metainformationen, wenn diese die Validierung durchläufen.
     *
     * @param datePollMetaInf die Metainformationen.
     * @return Referenz auf diesen DatePollBuilder.
     */
    public DatePollBuilder datePollMetaInf(DatePollMetaInf datePollMetaInf) {
        validationProcessAndValidationHandling(
                datePollMetaInf, metaInf -> this.metaInfTarget = metaInf, PollFields.DATE_POLL_META_INF
        );
        return this;
    }

    /**
     * Setzt den Ersteller, wenn dieser die Validierung durchläuft.
     *
     * @param creator der Ersteller einer Terminfindung.
     * @return Referenz auf diesen DatePollBuilder.
     */
    public DatePollBuilder creator(UserId creator) {
        validationProcessAndValidationHandling(
                creator, id -> this.pollCreatorTarget = id, PollFields.CREATOR
        );
        return this;
    }

    /**
     * Setzt die Configuration, wenn diese die Validierung durchläuft.
     *
     * @param datePollConfig die Konfiguration.
     * @return Referenz auf diesen DatePollBuilder.
     */
    public DatePollBuilder datePollConfig(DatePollConfig datePollConfig) {
        validationProcessAndValidationHandling(
                datePollConfig, config -> this.configTarget = config, PollFields.DATE_POLL_CONFIG
        );
        if (validatedFields.contains(PollFields.DATE_POLL_CONFIG)) {
            if (datePollConfig.isOpen()) {
                validatedFields.add(PollFields.PARTICIPATING_GROUPS);
            } else {
                validatedFields.remove(PollFields.PARTICIPATING_GROUPS);
            }
        }
        return this;
    }

    // TODO: Law of Demeter reviewen und Methode ggfs anpassen. Warum werden die Abstimmungsdaten doppelt gespeichert?!
    @SuppressWarnings("PMD.LawOfDemeter")
    public DatePollBuilder datePollEntries(Set<DatePollEntry> datePollEntrySet) {
        this.pollEntryTargets.addAll(validateAllAndGetCorrect(
                datePollEntrySet,
                PollFields.DATE_POLL_ENTRIES
        ));
        if (!pollEntryTargets.isEmpty()) {
            validatedFields.add(PollFields.DATE_POLL_ENTRIES);
        }
        return this;
    }


    /**
     * Fügt alle Gruppen der Teilnehmerliste hinzu.
     *
     * @param participants Gruppen die zu dieser Terminfindung hinzugefügt werden sollen.
     * @return Referenz auf diesen DatePollBuilder.
     */
    public DatePollBuilder participatingGroups(Set<GroupId> participants) {
        this.participatingGroupsTargets.addAll(validateAllAndGetCorrect(participants, PollFields.PARTICIPATING_GROUPS));
        if (!this.participatingGroupsTargets.isEmpty()) {
            validatedFields.add(PollFields.PARTICIPATING_GROUPS);
        }
        return this;
    }

    /**
     * Setzt den Link, wenn dieser die Validierung durchläuft.
     *
     * @param pollLink der veröfentlichungs Link.
     * @return Referenz auf diesen DatePollBuilder.
     */
    public DatePollBuilder datePollLink(PollLink pollLink) {
        validationProcessAndValidationHandling(
                pollLink, link -> this.linkTarget = link, PollFields.POLL_LINK
        );
        return this;
    }

    /**
     * Baut das DatePoll Objekt, wenn alle Konstruktionssteps mind. 1 mal erfolgreich waren.
     *
     * @return Ein DatePoll Objekt in einem validen State.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.GuardLogStatement"})
    public DatePoll build() {
        if (validationState.hasNoErrors() && validatedFields.equals(VALID_SET)) {
            return new DatePoll(
                    new DatePollRecordAndStatus(),
                    metaInfTarget,
                    pollCreatorTarget,
                    configTarget,
                    pollEntryTargets, //new HashSet<>(), war fuer die UserIds gedacht
                    participatingGroupsTargets,
                    new HashSet<>(),
                    linkTarget
            );
        } else {
            final EnumSet<FieldErrorNames> errorNames = validationState.getErrorMessages();
            log.debug(SEPERATOR);
            log.debug(DATE_POLL_BUILD_FAILED);
            for (final FieldErrorNames error : errorNames) {
                log.debug(FAILED_TO_VALIDATE + error.toString());
            }
            VALID_SET.stream()
                    .filter(Predicate.not(validatedFields::contains))
                    .forEach(notSet -> log.debug(FIELD_NOT_PRESENT + notSet.name()));
            log.debug(SEPERATOR);
            throw new IllegalStateException(COULD_NOT_CREATE);
        }
    }
}
