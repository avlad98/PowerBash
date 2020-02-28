import java.io.IOException;

/**
 * Aceasta clasa reprezinta comanda "Cd"
 */
public class Cd implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Cd() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Iau path-ul comenzii si il impart in token-uri pentru parsare */
        String path = Argument.getArgument(1);
        String[] tokens = path.split("/");

        /* Navighez cu folderul temporar la calea impartita in token-uri */
        if(FSManager.goTo(tokens) == 0){
            /* Daca a reusit navigarea atunci setez folderul curent la folderul temporar */
            FSManager.setCurrent(FSManager.getVirtualCurrent());
        }else {
            /* Daca nu a reusit navigarea inseamna ca folderul nu exista si afisez eroarea corespunzatoare */
            Main.errors.write("cd: " + path + ": No such directory" + "\n");
            return;
        }
    }
}
