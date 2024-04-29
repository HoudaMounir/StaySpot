package com.example.houda.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(int id){
        super("Property not found with id " + id);

    }
}
