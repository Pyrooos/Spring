package com.aluracursos.screenmatch.service;

public interface IConvertData {
    <T> T getData(String json, Class <T> clase);
}
