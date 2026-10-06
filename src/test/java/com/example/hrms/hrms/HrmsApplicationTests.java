package com.example.hrms.hrms;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

class HrmsApplicationTests {
  @Test
  void applicationEntryPointIsConfiguredForSpringBoot() {
    assertNotNull(HrmsApplication.class.getAnnotation(SpringBootApplication.class));
  }
}
