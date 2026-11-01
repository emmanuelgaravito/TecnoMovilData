import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TecnoMovilService {

    // a) Afluencia por estación (Función pura con Parallel Stream)
    public static Map<String, Long> calcularAfluenciaPorEstacion(List<RegistroValidador> registros) {
        return registros.parallelStream()
                .filter(r -> "entrada".equalsIgnoreCase(r.accion()))
                .collect(Collectors.groupingBy(
                        RegistroValidador::estacion,
                        Collectors.counting()
                ));
    }

    // b) Identificación de horas pico
    public static Map<Integer, Long> identificarHorasPico(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        r -> r.timestamp().getHour(),
                        Collectors.counting()
                ));
    }

    // c) Rutas más utilizadas
    public static Map<String, Long> obtenerUsoPorRuta(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroValidador::ruta,
                        Collectors.counting()
                ));
    }

    // d) Patrones de viaje por usuario (Ordenamiento cronológico declarativo)
    public static Map<String, List<String>> obtenerPatronesDeViajePorUsuario(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroValidador::idUsuario,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> lista.stream()
                                        .sorted(Comparator.comparing(RegistroValidador::timestamp))
                                        .map(RegistroValidador::estacion)
                                        .toList()
                        )
                ));
    }

    // e) Tiempo promedio entre estaciones (100% declarativo con IntStream.range)
    public static double calcularTiempoPromedioEntreEstaciones(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroValidador::idUsuario,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                lista -> lista.stream()
                                        .sorted(Comparator.comparing(RegistroValidador::timestamp))
                                        .toList()
                        )
                ))
                .values().stream()
                .flatMap(viajes -> IntStream.range(0, viajes.size() - 1)
                        .mapToObj(i -> Duration.between(
                                viajes.get(i).timestamp(),
                                viajes.get(i + 1).timestamp()
                        ).toMinutes())
                )
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
    }

    // f) Detección de sobrecarga en rutas
    public static Map<String, String> detectarRutasCriticas(List<RegistroValidador> registros, long umbralOcupacion) {
        return obtenerUsoPorRuta(registros).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() > umbralOcupacion ? "crítica" : "normal"
                ));
    }
}