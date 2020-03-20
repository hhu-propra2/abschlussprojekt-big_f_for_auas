package mops.adapters.datepolladapter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

@Data
@AllArgsConstructor
public final class OptionDto implements Comparable<OptionDto>, Serializable {

    public static final long serialVersionUID = 575665475776735L;

    private String date;
    private String startTime;
    private String endTime;

    @SuppressWarnings("PMD.LawOfDemeter")
    @Override
    public int compareTo(OptionDto optionDto) {
        return (int) (LocalDate.parse(this.date)
                .atTime(LocalTime.parse(this.startTime))
                .toEpochSecond(ZoneOffset.MIN)
                - LocalDate.parse(optionDto.getDate())
                .atTime(LocalTime.parse(optionDto.getStartTime()))
                .toEpochSecond(ZoneOffset.MIN));
    }
}
