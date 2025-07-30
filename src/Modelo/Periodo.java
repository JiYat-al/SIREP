package Modelo;

public enum Periodo {
    ENERO_JUNIO("Enero - Junio"),
    JULIO_AGOSTO("Julio - Agosto"),
    AGOSTO_DICIEMBRE("Agosto - Diciembre");

    private final String etiqueta;

    Periodo(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}

