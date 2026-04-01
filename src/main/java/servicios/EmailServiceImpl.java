package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailServiceImpl implements InterfazEnviarEmails {
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public boolean enviarEmail(Destinatario dest, String contenido) {
        // Importante: El nombre y los parámetros (Destinatario, String)
        // deben ser exactos a la interfaz
        String url = "http://localhost:8080/Email?nombreUsuario=usuario_izan&destinatario=" + dest.getEmail();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(contenido))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}