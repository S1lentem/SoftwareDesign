package com.example.artem.softwaredesign.data.exceptions.validation;

import java.util.List;

public class EmptyFieldException extends Exception {
    private String[] fields;


    public EmptyFieldException(String ... fields){
        this.fields = fields;
    }

    public EmptyFieldException(List<String> fields){
        this.fields = new String[fields.size()];
        this.fields = fields.toArray(this.fields);
    }

    @Override
    public String getMessage(){
        StringBuilder message = new StringBuilder();
        for (String field: fields) {
            message.append(field).append(", ");
        }
        int length = message.length();
        message.replace(length -2, length-1, "");
        return String.format("%s can not be empty", message);
    }
}
