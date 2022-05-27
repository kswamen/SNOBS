package com.back.snobs.domain.snob.dailyLog;

import com.back.snobs.domain.log.Log;
import com.back.snobs.domain.snob.Snob;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@RequiredArgsConstructor
@Entity
@DynamicInsert
@IdClass(DailyLogId.class)
public class DailyLog {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "userEmail")
    private Snob snob;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "logIdx")
    private Log log;

    @Builder
    public DailyLog(Snob snob, Log log) {
        this.log = log;
        this.snob = snob;
    }
}
