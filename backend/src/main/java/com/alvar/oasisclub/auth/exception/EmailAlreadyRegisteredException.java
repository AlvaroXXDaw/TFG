package com.alvar.oasisclub.auth.exception;

public class EmailAlreadyRegisteredException extends RuntimeException {

  public EmailAlreadyRegisteredException(String message) {
    super(message);
  }
}

