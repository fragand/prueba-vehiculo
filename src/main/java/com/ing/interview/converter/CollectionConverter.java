package com.ing.interview.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectionConverter {
    private ConversionService conversionService;

    @Autowired
    public CollectionConverter(ConversionService conversionService){
        this.conversionService = conversionService;
    }

    public <R,I> List<R> fromListToList(List<I> toConvert, Class<I> sourceType, Class<R> targetType) {
        TypeDescriptor sourceListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(sourceType));
        TypeDescriptor targetListType = TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(targetType));
        return (List<R>)conversionService.convert(toConvert, sourceListType, targetListType);
    }
}