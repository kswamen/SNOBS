package com.back.snobs.domain.snob.dailyLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyLogId implements Serializable {
    private Long snob;
    private Long log;
}
