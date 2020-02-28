import java.io.IOException;

/**
 * Aceasta clasa reprezinta comanda "Rm"
 */
public class Rm implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Rm() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Despart argumentul si obtin calea catre fisier/folder si numele fisierului/folderului */
        String pathToRemove = Argument.getArgument(1);
        String[] tokensAll = pathToRemove.split("/");
        String[] tokensPath = FSManager.getPathTo(tokensAll);
        String fileName = tokensAll[tokensAll.length - 1];

        if(fileName.equals(".") || fileName.equals("..")) { return; }

        /* Calea catre folderul sau fisierul de eliminat */
        String path = FSManager.getErrorMsg(tokensPath);

        /* Mut folderul temporar la calea catre folderul sau fisierul de eliminat */
        if(FSManager.goTo(tokensPath) != 0) {
            Main.errors.write("rm: cannot remove " + pathToRemove + ": No such file or directory\n");
            return;
        }

        /* Iau fisierul sau folderul de sters */
        FSNode toRemove = FSManager.getVirtualCurrent().findFile(fileName);

        /* Daca nu exista afisez mesajul corespunzator */
        if(toRemove == null) {
            Main.errors.write("rm: cannot remove " + pathToRemove + ": No such file or directory\n");
            return;
        }

        if(toRemove.checkIfContainsCurrent()) {
            /* Daca folderul de sters contine folderul curent atunci nu se va intampla nimic */
            return;
        }

        /* Verific daca trebuie sters un fisier sau folder */
        if(toRemove.getType().equals("File")) {
            FSManager.getVirtualCurrent().remove(toRemove);

            /* Daca stergerea s-a realizat cu succes atunci opresc executia comenzii */
            return;
        }else {
            /* Daca s-a ajuns aici inseamna ca trebuie sters un folder */
            /* Verific daca folderul de sters este sau contine folderul curent din sistemul de fisiere */
            if(toRemove == FSManager.getCurrent()) {
                /* Daca folderul de sters este chiar folderul curent atunci nu se va intampla nimic */
                return;
            }

            /* Altfel se va sterge folderul */
            FSManager.getVirtualCurrent().remove(toRemove);
        }
    }
}
