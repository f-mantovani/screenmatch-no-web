package com.felipe.screenmatch.service;

public interface IDataConversion {
    <T> T getData(String json, Class<T> tClass);
}
