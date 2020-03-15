package mops.application.services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import mops.domain.models.Validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mops.domain.models.questionpoll.*;
import mops.domain.models.user.UserId;

@RequiredArgsConstructor
@Data
public class QuestionPollBuilderAndView {

    private final QuestionPollBuilder builder;
    private QuestionPollConfig config;
    private QuestionPollHeader header;
    private List<QuestionPollEntry> questionPollEntries;
    private QuestionPollLifecycle lifecycle;
    private final Set<UserId> participants = new HashSet<>();
    private boolean accessRestriction;
    private Validation validation;

    /**
     * Methode dient zur erstellung des QuestionPoll Objektes.
     *
     * @return QuestionpPoll Objekt, welches den derzeitigen Status des Builders implementiert
     */
    public QuestionPoll startBuildingQuestionPoll() {
        return this.builder.build();
    }

}
