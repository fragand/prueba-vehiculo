package com.ing.interview.infrastructure;

import com.ing.interview.model.CarApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CarApplicationRepository extends JpaRepository<CarApplication, Long> {

    CarApplication findCarApplicationByAgeAndColorAndModel(int age, String color, String model);

    List<CarApplication> findCarApplicationsByColor(String color);

}