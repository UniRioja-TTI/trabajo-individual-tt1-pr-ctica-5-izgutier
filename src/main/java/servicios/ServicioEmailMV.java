package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class ServicioEmailMV implements InterfazEnviarEmails {
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public boolean enviarEmail(Destinatario dest, String contenido) {
        String url = "http://localhost:8080/Email?user=usuario_izan&to=" + dest.getEmail();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(contenido))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Si devuelve 200, el email se gestionó correctamente en la MV
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error en el servicio de Email: " + e.getMessage());
            return false;
        }
    }
}