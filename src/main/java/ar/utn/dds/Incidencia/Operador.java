package ar.utn.dds.Incidencia;

public class Operador {

    String nombre;

    public Operador (String nombre){
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "nombre='" + nombre + '\'';
    }
}