package com.ing.interview.integration.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

@Service
public class EndpointUtil {
    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    public ResultActions getCall(String path) throws Exception {
        return mockMvc.perform(addBasicInformation(MockMvcRequestBuilders.get(path)));
    }

    public ResultActions postCall(String path, Object content) throws Exception {
        return mockMvc.perform(addBasicInformation(MockMvcRequestBuilders.post(path).content(mapper.writeValueAsString(content))));
    }

    public <T> T getResponseObject(MvcResult result, Class<T> t) throws IOException {
        String contentAsString = result.getResponse().getContentAsString();
        return mapper.readValue(contentAsString, t);
    }

    public <T> T getResponseObject(MvcResult result, TypeReference<T> t) throws IOException {
        String contentAsString = result.getResponse().getContentAsString();
        return mapper.readValue(contentAsString, t);
    }

    private MockHttpServletRequestBuilder addBasicInformation(MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
        return mockHttpServletRequestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }
}