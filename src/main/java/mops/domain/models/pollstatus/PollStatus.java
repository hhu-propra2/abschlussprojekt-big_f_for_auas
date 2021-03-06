package mops.domain.models.pollstatus;

import lombok.Getter;

public enum PollStatus {
    OPEN("fa-hourglass"),
    REOPENED("fa-exclamation"),
    ONGOING("fa-check"),
    TERMINATED("fa-eye");

    @Getter
    private final String iconName;

    PollStatus(String iconName) {
        this.iconName = iconName;
    }
}
