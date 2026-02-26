package com.alvar.oasisclub;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BackendApplicationTests {

  @Test
  void applicationClassExists() {
    assertDoesNotThrow(() -> Class.forName("com.alvar.oasisclub.BackendApplication"));
  }
}

