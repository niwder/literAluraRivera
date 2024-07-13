package com.alura.literAlura.services;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}