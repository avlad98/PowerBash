import java.io.IOException;

/**
 * Aceasta clasa reprezinta comanda "Touch"
 */
public class Touch implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Touch() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia.
     * Functionalitatea acestei comenzi este asemanatoare cu cea a comenzii "Mkdir" doar ca se aplica pe fisiere
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Iau argumentul din lista de argumente si impart path-ul in token-uri pentru navigarea catre folderul din cale */
        String arg = Argument.getArgument(1);
        String[] tokens = arg.split("/");
        String fileName = tokens[tokens.length - 1];
        String[] errTokens = FSManager.getPathTo(tokens);
        String errPath = FSManager.getErrorMsg(errTokens);

        FSFolder folder = FSManager.getCurrent();

        /* Navighez cu folderul temporar la calea unde se va crea fisierul */
        for(int i = 0; i < (tokens.length - 1); i++) {
            if(tokens[i].equals("")) {
                /* Pornesc din root */
                FSManager.setVirtualCurrent(FSManager.getRoot());
            }else if(tokens[i].equals("..")) {
                /* Ma mut un folder inapoi */
                if (FSManager.stepBack()) {
                    /* si nu fac nimic */
                } else {
                    /* Daca nu a reusit pasul inapoi inseamna ca folderul nu exista */
                    Main.errors.write("touch: " + errPath + ": No such directory" + "\n");
                    return;
                }
            }else if(tokens[i].equals(".")) {
                /* Nu se schimba folderul */
            }else {
                /* Iau folderul care corespunde caii unde am navigat si caut urmatorul folder */
                folder = FSManager.getVirtualCurrent();
                FSFolder nextFolder = (FSFolder)folder.findFile(tokens[i]);

                if((nextFolder == null) || !(folder.findFile(tokens[i]) instanceof FSFolder)) {
                    /* Daca folderul nu exista sau nodul nu este folder (este fisier) se afiseaza eroarea corespunzatoare */
                    Main.errors.write("touch: " + errPath + ": No such directory" + "\n");
                    return;
                }

                /* Daca am ajuns aici inseamna ca folderul a fost gasit si ma mut cu folderul temporar pe el */
                FSManager.setVirtualCurrent(nextFolder);
            }
        }

        /* Iau folderul la care am navigat */
        folder = FSManager.getVirtualCurrent();

        /* Caut daca exista nodul deja in folder*/
        if(folder.findFile(fileName) != null) {
            /* Daca exista afisez eroarea corespunzatoare existentei */
            String completare;

            if(arg.charAt(0) == '/' || arg.charAt(0) == '.') {
                completare = "";
            }else {
                completare = FSManager.getCurrentPath();

                if(FSManager.getCurrent() != FSManager.getRoot()) {
                    completare += "/";
                }
            }

            Main.errors.write("touch: cannot create file " + completare + arg + ": Node exists" + "\n");
            return;
        }else {
            /* Altfel creez un nou fisier cu campurile corespunzatoare si il adaug in continutul folderului din path */
            FSFile newFile = new FSFile(fileName, folder);
            folder.getContent().put(fileName, newFile);
        }
    }
}
