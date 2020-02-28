import java.io.IOException;

/**
 * Aceasta clasa reprezinta comanda "Mkdir"
 */
public class Mkdir implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Mkdir() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Iau argumentul din lista de argumente si impart path-ul in token-uri pentru navigarea catre folderul din cale */
        String firstArg = Argument.getArgument(1);
        String[] tokens = firstArg.split("/");

        /* Retin numele folderului */
        String folderName = tokens[tokens.length - 1];

        /* Formeaz calea catre folderul de creat */
        String[] thePath = FSManager.getPathTo(tokens);

        /* Navighez cu folderul temporar la calea creata mai sus si verific codul intors */
        if(FSManager.goTo(thePath) != 0) {
            Main.errors.write("mkdir: " + FSManager.getErrorMsg(thePath) + ": No such directory" + "\n");
            return;
        }

        FSFolder temp = FSManager.getVirtualCurrent();

        /* Creeaza folderul dorit dupa ce am ajuns in folderul din <path>
        * Daca folderul/fisierul exista atunci se va afisa eroarea corespunzatoare*/
        if(temp.findFile(folderName) != null) {
            String completare;

            if(firstArg.charAt(0) == '/' || firstArg.charAt(0) == '.') {
                completare = "";
            }else {
                completare = FSManager.getCurrentPath();
            }

            Main.errors.write("mkdir: cannot create directory " + completare + firstArg + ": Node exists" + "\n");
            return;
        }else {
            /* Daca navigarea a reusit si folderul nu exista atunci se va crea unul nou corespunzator. Acesta va fi
             * adaugat in continutul folderului din path (cel care trebuie sa contina folderul nou) */
            FSFolder newFolder = new FSFolder(folderName, temp);
            temp.getContent().put(folderName, newFolder);
        }

    }
}
