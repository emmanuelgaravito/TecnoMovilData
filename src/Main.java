import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<RegistroValidador> datos = List.of(
                new RegistroValidador("U1", "Ruta 101", "Estación Central", "entrada", LocalDateTime.of(2026, 11, 1, 7, 15)),
                new RegistroValidador("U1", "Ruta 101", "Estación Norte", "salida", LocalDateTime.of(2026, 11, 1, 7, 45)),
                new RegistroValidador("U2", "Ruta 102", "Estación Sur", "entrada", LocalDateTime.of(2026, 11, 1, 8, 5)),
                new RegistroValidador("U2", "Ruta 102", "Estación Central", "salida", LocalDateTime.of(2026, 11, 1, 8, 35)),
                new RegistroValidador("U3", "Ruta 101", "Estación Central", "entrada", LocalDateTime.of(2026, 11, 1, 7, 30))
        );

        System.out.println("=== TECNOMÓVIL DATA - PROCESAMIENTO FUNCIONAL ===");
        System.out.println("\n--- Afluencia por Estación ---");
        TecnoMovilService.calcularAfluenciaPorEstacion(datos)
                .forEach((estacion, total) -> System.out.println(estacion + ": " + total + " ingresos"));

        System.out.println("\n--- Horas Pico ---");
        TecnoMovilService.identificarHorasPico(datos)
                .forEach((hora, total) -> System.out.println(hora + ":00 hrs -> " + total + " registros"));

        System.out.println("\n--- Patrones de Viaje por Usuario ---");
        TecnoMovilService.obtenerPatronesDeViajePorUsuario(datos)
                .forEach((usuario, estaciones) -> System.out.println(usuario + ": " + estaciones));

        System.out.println("\n--- Tiempo Promedio entre Estaciones ---");
        System.out.println("Promedio: " + TecnoMovilService.calcularTiempoPromedioEntreEstaciones(datos) + " minutos");

        System.out.println("\n--- Sobrecarga en Rutas (Umbral = 1) ---");
        TecnoMovilService.detectarRutasCriticas(datos, 1)
                .forEach((ruta, estado) -> System.out.println(ruta + " -> Estado: " + estado));
    }
}