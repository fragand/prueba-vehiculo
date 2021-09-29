package com.ing.interview.model;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Builder
@Getter
public class CarApplicationRequest {

    @NonNull
    private final int age;

    private final String color;

    @NonNull
    @NotBlank
    private final String model;

}
