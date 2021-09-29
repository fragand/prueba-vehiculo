package com.ing.interview.presentation;

import com.ing.interview.model.CarApplicationRequest;
import com.ing.interview.model.CarApplicationResponse;
import com.ing.interview.service.CarApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@Api(tags = "Car applications Endpoint")
@RestController
public class CarApplicationController {

    private final CarApplicationService carApplicationService;


    @Autowired
    public CarApplicationController(CarApplicationService carApplicationService) {
        this.carApplicationService = carApplicationService;
    }

    @ApiOperation(value = "Creates a car application based of the request object.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful created car", response = CarApplicationResponse.class),
            @ApiResponse(code = 422, message = "There was an issue processing the entity."),
            @ApiResponse(code = 500, message = "Unknown error on service")})
    @PostMapping(path="/car-applications",consumes= {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public CarApplicationResponse createCarApplication(
            @ApiParam(value = "The car application to create", required = true)
            @Valid
            @RequestBody CarApplicationRequest carApplicationRequest) {
        return carApplicationService.createCarApplication(carApplicationRequest);
    }

    @ApiOperation(value = "Returns the specified car applications.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The car application.", response = CarApplicationResponse.class),
            @ApiResponse(code = 404, message = "The car application doesn't exists"),
            @ApiResponse(code = 500, message = "Unknown error")})
    @GetMapping("/car-applications/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarApplicationResponse getCarApplication(
            @ApiParam(value = "The car application ID", example = "1",	required = true)
            @PathVariable("id")
            @Min(1) Long id){
        return carApplicationService.getCarApplication(id);
    }

    @ApiOperation(value = "Returns the specified car application for an specific color.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The car applications.", response = CarApplicationResponse.class),
            @ApiResponse(code = 500, message = "Unknown error")})
    @GetMapping("/car-applications/colors/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<CarApplicationResponse> getCarApplicationByColor(
            @ApiParam(value = "The color", required = true)
            @PathVariable("color")
            String color){
        return carApplicationService.getCarApplicationByColor(color);
    }


}
