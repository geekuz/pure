package com.geekuz.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Examples of basic unit testing with JUnit 5
 */
public class UnitTesting {
    
    // Class to be tested
    static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
        
        public int divide(int a, int b) {
            if (b == 0) {
                throw new ArithmeticException("Cannot divide by zero");
            }
            return a / b;
        }
    }
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        // This runs before each test
        calculator = new Calculator();
        System.out.println("Test started");
    }
    
    @AfterEach
    void tearDown() {
        // This runs after each test
        System.out.println("Test finished");
    }
    
    @Test
    @DisplayName("Simple addition test")
    void testAddition() {
        int result = calculator.add(2, 3);
        Assertions.assertEquals(5, result, "2 + 3 should equal 5");
    }
    
    @Test
    @DisplayName("Division test")
    void testDivision() {
        int result = calculator.divide(10, 2);
        Assertions.assertEquals(5, result);
    }
    
    @Test
    @DisplayName("Division by zero should throw exception")
    void testDivisionByZero() {
        Assertions.assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        });
    }
}
