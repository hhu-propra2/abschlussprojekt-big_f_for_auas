package mops.adapters.datepolladapter.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigDto extends GeneralDto implements Serializable {

    public static final long serialVersionUID = 43545553524L;

    private boolean openForOwnEntries;
    private boolean singleChoice;
    private boolean priorityChoice;
    private boolean anonymous;
    private boolean open;
}
