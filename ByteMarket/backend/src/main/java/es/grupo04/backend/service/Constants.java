package es.grupo04.backend.service;

import java.util.Set;

public final class Constants {

    private Constants() {
    }

    //Develop
    //public static final String WEBAPP_BASE_URL = "https://localhost:8443";

    //Production
    public static final String WEBAPP_BASE_URL = "https://appweb04.dawgis.etsii.urjc.es:443";

    public static final Set<String> VALID_CATEGORIES = Set.of(
        "Móviles",
        "Auriculares",
        "Ordenadores",
        "SmartWatches",
        "Otros"
    );

    public static final Set<String> VALID_REASONS = Set.of(
        "Producto Dañado",
        "Producto Diferente",
        "Faltan Piezas",
        "Otro"
    );
}
