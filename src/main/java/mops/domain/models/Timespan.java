package mops.domain.models;

import lombok.Value;
import mops.controllers.dtos.InputFieldNames;

import java.time.LocalDateTime;

@Value
public class Timespan {

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * Stellt sicher, dass das startDate vor dem endDate liegt.
     * @return Validation-Objekt mit oder ohne Error
     */
    public Validation validate() {
        final Validation validation = Validation.noErrors();
        if (endDate.isBefore(startDate)) {
            validation.appendValidation(new Validation(InputFieldNames.TIMESPAN_INVALID, ""));
        }
        return validation;
    }
}
