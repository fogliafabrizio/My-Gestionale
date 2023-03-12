package it.fogliafabrizio.mygestionale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventRequest {
    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private String eventVisibility;
    private LocalDate eventDate;
    private LocalTime eventStartTime;
    private LocalTime eventEndTime;
    private boolean allDayEvent;
    private List<Long> userIds;
    private List<Long> groupIds;
}
