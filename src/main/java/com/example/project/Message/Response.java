package com.example.project.Message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class Response {

    private boolean success;

    private String responseCode;

    private String message;

    private Object error;

    private Object data;
}
