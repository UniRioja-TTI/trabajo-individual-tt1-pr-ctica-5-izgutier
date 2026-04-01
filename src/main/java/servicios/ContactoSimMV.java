package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ContactoSimMV implements InterfazContactoSim {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String BASE_URL = "http://localhost:8080";

    @Override
    public int solicitarSimulation(DatosSolicitud sol) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Solicitud/Solicitar"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(sol.getNums().toString()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return Integer.parseInt(response.body().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public DatosSimulation descargarDatos(int ticket) {
        String url = BASE_URL + "/Resultados?nombreUsuario=usuario_izan";
        String cuerpoJson = "{\"tokenSolicitud\":" + ticket + "}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "*/*")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("--- ERROR DE INTEGRACIÓN ---");
                System.err.println("URL: " + url);
                System.err.println("Cuerpo: " + cuerpoJson);
                System.err.println("Respuesta MV: " + response.body());
                return null;
            }
            String[] lineas = response.body().split("\n");
            DatosSimulation ds = new DatosSimulation();
            if (lineas.length > 0 && !lineas[0].trim().isEmpty()) {
                ds.setAnchoTablero(Integer.parseInt(lineas[0].trim()));
                Map<Integer, List<Punto>> puntosMap = new HashMap<>();
                for (int i = 1; i < lineas.length; i++) {
                    String linea = lineas[i].trim();
                    if (linea.isEmpty()) continue;
                    String[] p = linea.split(",");
                    if (p.length >= 4) {
                        Punto punto = new Punto();
                        int tiempo = Integer.parseInt(p[0].trim());
                        punto.setY(Integer.parseInt(p[1].trim()));
                        punto.setX(Integer.parseInt(p[2].trim()));
                        punto.setColor(p[3].trim());

                        puntosMap.computeIfAbsent(tiempo, k -> new ArrayList<>()).add(punto);
                    }
                }
                ds.setPuntos(puntosMap);
            }
            return ds;
        } catch (Exception e) {
            System.err.println("Error en la conexión HTTP: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Entidad> getEntities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isValidEntityId(int id) {
        return id > 0;
    }
}