import java.time.LocalDateTime;

/**
 * Record inmutable para representar la validación de un usuario.
 */
public record RegistroValidador(
        String idUsuario,
        String ruta,
        String estacion,
        String accion, // "entrada" o "salida"
        LocalDateTime timestamp
) {}