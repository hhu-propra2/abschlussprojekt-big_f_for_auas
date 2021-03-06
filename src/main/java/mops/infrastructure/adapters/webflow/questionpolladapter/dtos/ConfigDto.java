package mops.infrastructure.adapters.webflow.questionpolladapter.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mops.infrastructure.adapters.webflow.dtos.GeneralDto;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigDto extends GeneralDto implements Serializable {

    public static final long serialVersionUID = 1L;

    private boolean singleChoice;
    private boolean anonymous;
    private boolean open;
}
