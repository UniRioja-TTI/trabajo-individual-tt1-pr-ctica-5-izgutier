package servicios;
import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import interfaces.InterfazEnviarEmails;

@Service
public class EmailServiceImpl implements InterfazEnviarEmails {
    private final Logger logger;

    public EmailServiceImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public boolean enviarEmail(String direccion, String mensaje) {
        logger.info("Enviando email a {}: {}", direccion, mensaje);
    }


    @Override
    public boolean enviarEmail(Destinatario dest, String email) {
        return false;
    }
}
