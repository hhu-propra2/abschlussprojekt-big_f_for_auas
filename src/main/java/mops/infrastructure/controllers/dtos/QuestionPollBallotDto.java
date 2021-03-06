package mops.infrastructure.controllers.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import mops.domain.models.user.UserId;

/**
 * QuestionPollBallotDto.
 */
@Data
@AllArgsConstructor
public class QuestionPollBallotDto {
  private UserId user;
  private List<QuestionPollEntryDto> selectedEntries;
}
