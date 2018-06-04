package com.example.victor.appubicacion;

public class UserInformation {
    private  String name;

    private String ubicacion;
    double longitud;
    double latitud;

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    private boolean compartir_ubicacion;



    public boolean isCompartir_ubicacion() {
        return compartir_ubicacion;
    }

    public void setCompartir_ubicacion(boolean compartir_ubicacion) {
        this.compartir_ubicacion = compartir_ubicacion;
    }
    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
