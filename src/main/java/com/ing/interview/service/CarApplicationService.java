package com.ing.interview.service;

import com.ing.interview.converter.CollectionConverter;
import com.ing.interview.exception.BadRequestErrorException;
import com.ing.interview.exception.BusinessValidationException;
import com.ing.interview.exception.NotFoundException;
import com.ing.interview.infrastructure.CarAvailabilityRestConnector;
import com.ing.interview.infrastructure.ColorPickerRestConnector;
import com.ing.interview.infrastructure.InsuranceRestConnector;
import com.ing.interview.infrastructure.OrderStatusRestConnector;
import com.ing.interview.model.CarApplication;
import com.ing.interview.model.CarApplicationRequest;
import com.ing.interview.model.CarApplicationResponse;
import com.ing.interview.repository.CarApplicationRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CarApplicationService {

    private final CarApplicationRepository carApplicationRepository;
    private final CarAvailabilityRestConnector carAvailabilityRestConnector;
    private final ColorPickerRestConnector colorPickerRestConnector;
    private final InsuranceRestConnector insuranceRestConnector;
    private final ConversionService conversionService;
    private final CollectionConverter collectionConverter;
    private final OrderStatusRestConnector orderStatusRestConnector;

    @Autowired
    public CarApplicationService(CarApplicationRepository carApplicationRepository, CarAvailabilityRestConnector carAvailabilityRestConnector,
                                 ColorPickerRestConnector colorPickerRestConnector, InsuranceRestConnector insuranceRestConnector,
                                 ConversionService conversionService, OrderStatusRestConnector orderStatusRestConnector,
                                 CollectionConverter collectionConverter) {
        this.carApplicationRepository = carApplicationRepository;
        this.carAvailabilityRestConnector = carAvailabilityRestConnector;
        this.colorPickerRestConnector = colorPickerRestConnector;
        this.insuranceRestConnector = insuranceRestConnector;
        this.conversionService = conversionService;
        this.orderStatusRestConnector = orderStatusRestConnector;
        this.collectionConverter = collectionConverter;
    }

    public CarApplicationResponse createCarApplication(CarApplicationRequest carApplicationRequest) {
        CarApplication carApplication = conversionService.convert(carApplicationRequest, CarApplication.class);
        checkCarColor(carApplication);
        checkIfCarIsAvailable(carApplication);
        checkInsurance(carApplication);
        carApplicationRepository.save(carApplication);
        return conversionService.convert(carApplication, CarApplicationResponse.class);
    }

    private void checkInsurance(CarApplication carApplication) {
        if(!insuranceRestConnector.isEligible(carApplication.getAge(), carApplication.getModel())){
            throw new BadRequestErrorException("car.notEligible.insurance", "The age and model of the car does not apply for insurance.");
        }
    }

    private void checkCarColor(CarApplication carApplication) {
        AtomicReference<String> color = new AtomicReference<>(carApplication.getColor());
        if(Strings.isBlank(color.get())){
            colorPickerRestConnector.pickColor(carApplication.getModel())
                    .ifPresent(color::set);

        }

        if(Strings.isBlank(color.get())){
            throw new BusinessValidationException("car.notColor", "No color was selected and no default color for the car.");
        }
        carApplication.setColor(color.get());
    }

    private void checkIfCarIsAvailable(CarApplication carApplication) {
        boolean available = carAvailabilityRestConnector.available(carApplication.getColor(),
                carApplication.getModel());
        if(!available){
            throw new BusinessValidationException("car.notAvailable", "The color and model has not availability.");
        }
    }

    public CarApplicationResponse getCarApplication(Long id) {
        CarApplication carApplication = carApplicationRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("carApplication.notFound", "there is no car application for the provided id"));
        CarApplicationResponse carApplicationResponse = conversionService.convert(carApplication, CarApplicationResponse.class);
        carApplicationResponse.setOrderStatus(orderStatusRestConnector.checkOrderStatus(carApplication.getId()));
        return carApplicationResponse;
    }

    public List<CarApplicationResponse> getCarApplicationByColor(String color) {
        List<CarApplication> carApplications = carApplicationRepository.findCarApplicationsByColorIgnoreCase(color);
        List<CarApplicationResponse> carApplicationResponses = collectionConverter.fromListToList(carApplications, CarApplication.class, CarApplicationResponse.class);
        carApplicationResponses.forEach(carApplicationResponse ->
                carApplicationResponse.setOrderStatus(orderStatusRestConnector.checkOrderStatus(carApplicationResponse.getId())));
        return carApplicationResponses;
    }
}
