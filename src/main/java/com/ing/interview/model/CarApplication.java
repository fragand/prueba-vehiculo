package com.ing.interview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CarApplication {

    @Column(name = "ID", nullable = false)
    @GeneratedValue(generator = "Sequence", strategy = SEQUENCE)
    @SequenceGenerator(name = "Sequence", sequenceName = "SEQUENCE_CAR", allocationSize = 1)
    @Id
    private Long id;

    private String color;

    private String model;

    private int age;

    private LocalDate orderDate;

}
