package com.pragma.restaurants.infrastructure.exception;

public class EmployeeNotAssignedException extends InfrastructureException {

    public EmployeeNotAssignedException(String message) {
        super("EMPLOYEE_NOT_ASSIGNED", message);
    }
}
