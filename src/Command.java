import java.io.IOException;

/**
 * Aceasta interfata este o componenta a design pattern-ului "Command" si are metoda execute pentru toate comenzile
 */
public interface Command {
    public void execute() throws IOException;
}
