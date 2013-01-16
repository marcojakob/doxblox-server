package com.documakery.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.documakery.dto.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Called when an exception occurs during request processing. Transforms the exception message into JSON format.
 */
@Component
public class JsonExceptionHandler implements HandlerExceptionResolver {
    private ObjectMapper mapper = new ObjectMapper();

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            mapper.writeValue(response.getWriter(), new ResponseMessage(ResponseMessage.Type.error, ex.getMessage()));
        } catch (IOException e) {
            //give up
            e.printStackTrace();
        }
        return new ModelAndView();
    }
}
