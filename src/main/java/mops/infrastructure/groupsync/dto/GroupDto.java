package mops.infrastructure.groupsync.dto;

import lombok.Data;

import java.util.Set;

@Data
public class GroupDto {

    private String id;
    private String title;
    private String description;
    private String visibility;

    private Set<UserDto> members;
}
