package com.ing.interview.repository;

import com.ing.interview.model.CarApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CarApplicationRepository extends JpaRepository<CarApplication, Long> {

    List<CarApplication> findCarApplicationsByColorIgnoreCase(String color);

}