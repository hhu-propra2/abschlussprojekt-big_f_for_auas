package mops.infrastructure.database.daos.datepoll;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class PriorityChoiceDaoKey implements Serializable {
    public static final long serialVersionUID = 6142932L;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "datepollentry_id")
    private Long datePollEntryId;
}
