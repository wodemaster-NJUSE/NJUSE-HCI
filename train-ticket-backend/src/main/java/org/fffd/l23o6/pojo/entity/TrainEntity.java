package org.fffd.l23o6.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import io.hypersistence.utils.hibernate.type.array.BooleanArrayType;

import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TrainEntity {
    public enum TrainType {
        HIGH_SPEED("高铁"), NORMAL_SPEED("普通列车");

        private String text;

        TrainType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static TrainType fromString(String text) {
            for (TrainType b : TrainType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long routeId;

    @NotNull
    @Type(BooleanArrayType.class)
    @Column(name = "seats", columnDefinition = "boolean[][]")
    private boolean[][] seats;

    @NotNull
    private TrainType trainType;

    @NotNull
    private String date;

    @NotNull
    private List<Date> arrivalTimes;

    @NotNull
    private List<Date> departureTimes;

    @NotNull
    private List<String> extraInfos;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
