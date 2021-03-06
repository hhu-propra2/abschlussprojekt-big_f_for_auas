package mops.domain.models;

public enum FieldErrorNames {


    DATE_POLL_TITLE_EMPTY(PollFields.DATE_POLL_META_INF),
    DATE_POLL_TITLE_TOO_LONG(PollFields.DATE_POLL_META_INF),
    DATE_POLL_DESCRIPTION_TOO_LONG(PollFields.DATE_POLL_META_INF),
    DATE_POLL_LOCATION_TOO_LONG(PollFields.DATE_POLL_META_INF),
    DATE_POLL_CONFIGURATION_CONFLICT(PollFields.DATE_POLL_CONFIG),
    DATE_POLL_IDENTIFIER_EMPTY(PollFields.POLL_LINK),
    DATE_POLL_IDENTIFIER(PollFields.POLL_LINK),

    TIMESPAN_SAME(PollFields.TIMESPAN),
    TIMESPAN_SWAPPED(PollFields.TIMESPAN),
    TIMESPAN_START_NULL(PollFields.TIMESPAN),
    TIMESPAN_END_NULL(PollFields.TIMESPAN),

    QUESTION_POLL_ENTRY_TITLE_IS_EMPTY(PollFields.QUESTION_POLL_ENTRY),
    QUESTION_POLL_ENTRY_TITLE_IS_ONLY_WHITESPACE(PollFields.QUESTION_POLL_ENTRY),
    QUESTION_POLL_ENTRY_TITLE_IS_TOO_LONG(PollFields.QUESTION_POLL_ENTRY),
    QUESTION_POLL_ENTRY_TITLE_IS_TOO_SHORT(PollFields.QUESTION_POLL_ENTRY),
    QUESTION_POLL_ENTRY_COUNT_IS_NEGATIVE(PollFields.QUESTION_POLL_ENTRY),

    QUESTION_POLL_HEADER_TITLE_IS_NULL(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_HEADER_TITLE_IS_EMPTY(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_HEADER_TITLE_IS_TOO_LONG(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_HEADER_TITLE_IS_TOO_SHORT(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_QUESTION_IS_NULL(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_QUESTION_IS_EMPTY(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_QUESTION_IS_TOO_LONG(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_QUESTION_IS_TOO_SHORT(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_DESCRIPTION_IS_TOO_LONG(PollFields.QUESTION_POLL_METAINF),
    QUESTION_POLL_DESCRIPTION_IS_TOO_SHORT(PollFields.QUESTION_POLL_METAINF);

    private final PollFields parent;

    FieldErrorNames(PollFields parent) {
        this.parent = parent;
    }

    public boolean isChildOf(PollFields field) {
        return this.parent == field;
    }

}
