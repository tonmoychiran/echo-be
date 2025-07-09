package com.example.goppho.responses;

public class GetResponse <T>extends Response{
    private T data;

    public GetResponse(String message, T data) {
        super(message);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
