package com.documakery.validation.dto;

/**
 * Contains the information of a single validation error. 
 * 
 * @author Marco Jakob
 */
public class ValidationErrorDto {

  private String path;
  private String message;

  /**
   * @param path identifies the property that did not pass the validation phase.
   * @param message the actual validation error message.
   */
  public ValidationErrorDto(String path, String message) {
    this.path = path;
    this.message = message;
  }

  public String getPath() {
    return path;
  }
  
  public String getMessage() {
    return message;
  }
}