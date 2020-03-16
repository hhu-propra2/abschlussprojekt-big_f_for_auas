package mops.domain.models.questionpoll;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import mops.controllers.dtos.InputFieldNames;
import mops.domain.models.ValidateAble;
import mops.domain.models.Validation;

/**
 * Speichert wann eine QuestionPoll über welchen Zeitraum die QuestionPoll offen für Abstimmungen ist.
 */
@Data
@AllArgsConstructor
public class QuestionPollLifecycle implements ValidateAble {
  private final Date start;
  private Date end;

  @Override
  public Validation validate() {
    Validation validator = Validation.noErrors();
    if (start.before(end)) {
      validator.appendValidation(new Validation(InputFieldNames.QUESTION_POLL_LIFECYLCE_START_BEFORE_END));
    }
  }
}
