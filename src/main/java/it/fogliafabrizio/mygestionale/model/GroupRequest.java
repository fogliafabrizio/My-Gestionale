package it.fogliafabrizio.mygestionale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupRequest {

    private String groupName;

    private String groupDescription;

    private Visibility groupVisibility;

    private List<Long> userIds;
}
