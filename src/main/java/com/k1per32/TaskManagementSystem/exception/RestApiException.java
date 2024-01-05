package com.k1per32.TaskManagementSystem.exception;

public class RestApiException extends AbstractBusinessLogicException {

  private final int code;
  private final String message;



  public RestApiException(final Integer code, final String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public RestApiException() {
    this.code = 500;
    this.message = "Failed with getting result from remote service";
  }

  @Override
  public String toString() {
    return "RestApiException{" +
            "code=" + code +
            ", message='" + message + '\'' +
            '}';
  }
}
