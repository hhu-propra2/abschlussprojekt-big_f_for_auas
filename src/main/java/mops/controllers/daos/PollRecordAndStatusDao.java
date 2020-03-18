package mops.controllers.daos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
class PollRecordAndStatusDao {
    @DateTimeFormat
    private LocalDateTime lastmodified;
    private boolean isterminated;
}
