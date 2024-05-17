package com.aluracursos.desafio.service;

public interface IDataConvert {
    <T> T getData(String json, Class <T> clase);
}
