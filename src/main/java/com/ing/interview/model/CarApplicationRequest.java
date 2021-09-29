package com.ing.interview.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@EqualsAndHashCode
public class CarApplicationRequest {

    @NonNull
    private final int age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
