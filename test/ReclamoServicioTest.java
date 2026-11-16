import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import modelo.Estudiante;
import modelo.ObjetoReportado;
import modelo.Prenda;
import modelo.Reclamo;
import repositorio.ObjetoReportadoCsvRepositorio;
import repositorio.ReclamoCsvRepositorio;
import repositorio.UsuarioCsvRepositorio;
import servicio.ObjetoServicio;
import servicio.ReclamoServicio;

public class ReclamoServicioTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Iniciando pruebas de flujo de reclamo y estados...");

        Path dirTemp = Paths.get("build/test-data");
        Files.createDirectories(dirTemp);
        Path archUsuarios = dirTemp.resolve("usuarios.csv");
        Path archObjetos = dirTemp.resolve("objetos.csv");
        Path archReclamos = dirTemp.resolve("reclamos.csv");

        Files.write(archUsuarios, List.of(
            "identificacion|nombre|correo|contrasena|facultad|carrera|ciclo|mensajes",
            "11111111|ESTUDIANTE A|a@udep.pe|pass1|INGENIERÍA|SISTEMAS|3|SIN_MENSAJES",
            "22222222|ESTUDIANTE B|b@udep.pe|pass2|HUMANIDADES|PSICOLOGÍA|5|SIN_MENSAJES",
            "33333333|ESTUDIANTE C|c@udep.pe|pass3|COMUNICACIÓN|PERIODISMO|2|SIN_MENSAJES"
        ));
        Files.write(archObjetos, List.of("id|nombre|descripcion|categoria|color|fecha|ubicacion|estado|reportado|caracteristicasPropias"));
        Files.write(archReclamos, List.of("id|idObjeto|codigoReclamante|respuestas|estado|observacion|codigoRevisor"));

        UsuarioCsvRepositorio usuarioRepo = new UsuarioCsvRepositorio(archUsuarios);
        ObjetoReportadoCsvRepositorio objetoRepo = new ObjetoReportadoCsvRepositorio(archObjetos, usuarioRepo);
        ReclamoCsvRepositorio reclamoRepo = new ReclamoCsvRepositorio(archReclamos, usuarioRepo, objetoRepo);

        ReclamoServicio reclamoServicio = new ReclamoServicio(reclamoRepo, objetoRepo, usuarioRepo);
        ObjetoServicio objetoServicio = new ObjetoServicio(objetoRepo);

        Estudiante estA = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion("11111111");
        Estudiante estB = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion("22222222");
        Estudiante estC = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion("33333333");

        // 1. Estudiante A notifica una casaca hallada
        int idObj = objetoServicio.generarNuevoId();
        Prenda casaca = new Prenda("L", "Nike", idObj, "Casaca negra", "Hallada en cafetin", "PRENDA", "NEGRO", "2026-11-13", "CAFETERÍA CENTRAL", "ENCONTRADO", estA);
        objetoServicio.registrarObjeto(casaca);

        ObjetoReportado objGuardado = objetoServicio.obtenerPorId(idObj);
        assert objGuardado != null : "El objeto no se guardó";
        assert "ENCONTRADO".equalsIgnoreCase(objGuardado.getEstado()) : "El estado inicial debe ser ENCONTRADO";
        System.out.println("✅ Objeto creado exitosamente con estado: " + objGuardado.getEstado());

        // 2. Estudiante A intenta reclamar su propio objeto (debe fallar)
        boolean reclamoPropio = reclamoServicio.crearReclamo(idObj, estA, List.of("L", "Nike"));
        assert !reclamoPropio : "No se debe permitir reclamar el propio objeto";
        System.out.println("✅ Validación correcta: El usuario que reportó no puede reclamarlo.");

        // 3. Estudiante B reclama el objeto
        boolean reclamoB = reclamoServicio.crearReclamo(idObj, estB, List.of("L", "Nike"));
        assert reclamoB : "El reclamo de Estudiante B debió ser exitoso";
        System.out.println("✅ Reclamo de Estudiante B creado exitosamente.");

        // 4. Verificar que el objeto cambió de estado a PENDIENTE
        ObjetoReportado objPostReclamo = objetoServicio.obtenerPorId(idObj);
        assert "PENDIENTE".equalsIgnoreCase(objPostReclamo.getEstado()) : "El estado del objeto debe ser PENDIENTE tras reclamo";
        System.out.println("✅ Estado del objeto actualizado automáticamente a: " + objPostReclamo.getEstado());

        // 5. Verificar que Estudiante B no puede reclamarlo de nuevo
        boolean reclamoRepetidoB = reclamoServicio.crearReclamo(idObj, estB, List.of("L", "Nike"));
        assert !reclamoRepetidoB : "Estudiante B no debe poder reclamar nuevamente mientras esté PENDIENTE";
        System.out.println("✅ Bloqueo correcto: Estudiante B no puede volver a reclamarlo.");

        // 6. Verificar que Estudiante C tampoco puede reclamarlo (ya está en PENDIENTE)
        boolean reclamoC = reclamoServicio.crearReclamo(idObj, estC, List.of("L", "Nike"));
        assert !reclamoC : "Estudiante C no debe poder reclamar un objeto que ya está PENDIENTE";
        System.out.println("✅ Bloqueo correcto: Otro estudiante no puede reclamar un objeto ya pendiente.");

        // 7. Verificar notificaciones a ambos estudiantes
        Estudiante estARecargado = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion("11111111");
        Estudiante estBRecargado = (Estudiante) usuarioRepo.obtenerUsuarioPorIdentificacion("22222222");

        boolean aTieneMensaje = estARecargado.getMensajes().stream().anyMatch(m -> m.contains("PENDIENTE"));
        boolean bTieneMensaje = estBRecargado.getMensajes().stream().anyMatch(m -> m.contains("PENDIENTE"));
        assert aTieneMensaje : "Estudiante A debió recibir notificación de reclamo pendiente";
        assert bTieneMensaje : "Estudiante B debió recibir confirmación de reclamo";
        System.out.println("✅ Notificaciones enviadas correctamente a ambas partes.");

        // 8. Probar rechazo de reclamo (el objeto debe volver a ENCONTRADO)
        int idObj2 = objetoServicio.generarNuevoId();
        Prenda pantalon = new Prenda("32", "Levis", idObj2, "Pantalón azul", "Hallada en CA 101", "PRENDA", "AZUL", "2026-11-13", "CA 101", "ENCONTRADO", estA);
        objetoServicio.registrarObjeto(pantalon);

        boolean reclamoPantalon = reclamoServicio.crearReclamo(idObj2, estB, List.of("32", "Levis"));
        assert reclamoPantalon : "Reclamo debió crearse";
        assert "PENDIENTE".equalsIgnoreCase(objetoServicio.obtenerPorId(idObj2).getEstado());

        List<Reclamo> recsPantalon = reclamoServicio.listarPorObjeto(idObj2);
        assert !recsPantalon.isEmpty();
        Reclamo recP = recsPantalon.get(0);

        boolean rechazado = reclamoServicio.rechazarReclamo(recP.getId(), estA.getIdentificacion(), "Datos no coinciden");
        assert rechazado : "Rechazo falló";
        ObjetoReportado objPostRechazo = objetoServicio.obtenerPorId(idObj2);
        assert "ENCONTRADO".equalsIgnoreCase(objPostRechazo.getEstado()) : "Tras rechazo, el objeto debe volver a ENCONTRADO";
        System.out.println("✅ Rechazo correcto: El objeto vuelve a estar ENCONTRADO (disponible).");

        // Ahora Estudiante C sí puede reclamarlo porque volvió a ENCONTRADO
        boolean reclamoCPantalon = reclamoServicio.crearReclamo(idObj2, estC, List.of("32", "Levis"));
        assert reclamoCPantalon : "Estudiante C debe poder reclamar tras el rechazo del anterior";
        System.out.println("✅ El objeto volvió a estar disponible y fue reclamado por Estudiante C.");

        // 9. Aprobar el reclamo por Estudiante A
        List<Reclamo> reclamos = reclamoServicio.listarPorObjeto(idObj);
        assert !reclamos.isEmpty() : "Debe existir al menos un reclamo";
        Reclamo rec = reclamos.get(0);

        boolean aprobado = reclamoServicio.aprobarReclamo(rec.getId(), estA.getIdentificacion(), "Verificación exitosa");
        assert aprobado : "Aprobación falló";

        ObjetoReportado objFinal = objetoServicio.obtenerPorId(idObj);
        assert "ENTREGADO".equalsIgnoreCase(objFinal.getEstado()) : "Estado final del objeto debe ser ENTREGADO";
        System.out.println("✅ Reclamo aprobado y objeto marcado como: " + objFinal.getEstado());

        System.out.println("\n🎉 ¡TODAS LAS PRUEBAS UNITARIAS PASARON EXITOSAMENTE!");
    }
}
