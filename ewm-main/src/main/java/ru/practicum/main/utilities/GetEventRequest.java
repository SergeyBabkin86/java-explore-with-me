package ru.practicum.main.utilities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GetEventRequest {

    private String text;
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private Sort sort;

    public static GetEventRequest of(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort) {
        GetEventRequest request = new GetEventRequest();
        request.setText(text);
        request.setCategories(categories);
        request.setPaid(paid);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);
        request.setOnlyAvailable(onlyAvailable);
        if (sort != null) {
            request.setSort(Sort.valueOf(sort));
        } else {
            request.setSort(Sort.EVENT_DATE);
        }
        return request;
    }

    public static GetEventRequest of(List<Long> users,
                                     List<String> states,
                                     List<Long> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd) {
        GetEventRequest request = new GetEventRequest();

        request.setUsers(users);
        request.setStates(states);
        request.setCategories(categories);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);

        return request;
    }

    public enum Sort {
        EVENT_DATE,
        VIEWS
    }
}
