package com.portfolio.coursesapi.dto.marvel;

public record MarvelThumbnail(String path, String extension) {
    // Método de utilidad para armar la URL completa de la imagen
    public String getFullUrl() {
        return path + "." + extension;
    }
}