import java.io.IOException;

/**
 * Aceasta clasa reprezinta comanda "Pwd"
 */
public class Pwd implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Pwd() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Pentru aceasta comanda doar se scrie path-ul folderului curent */
        Main.output.write(FSManager.getCurrentPath() + "\n");
    }
}
