import java.io.IOException;
import java.util.LinkedList;

/**
 * Aceasta clasa reprezinta comanda "Mv"
 */
public class Mv implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Mv() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia.
     * Comportamentul acestei comenzi este asemanator cu cel al comenzii "Cp" doar ca aici nu se va mai clona
     * nodul, ci se va muta referinta in nou path, iar din cel vechi va fi sters. De asemenea, se va muta si
     * folderul curent odata cu mutarea instantei in noul path (daca nu apar erorile mentionate in PDF-ul temei)
     * deoarece folderul curent este o referinta la aceasta instanta.
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        LinkedList<String> args = Argument.getArgs();

        /* Sursa */
        String srcPath = Argument.getArgument(1);
        String[] srcTokens = srcPath.split("/");
        String srcFileName = srcTokens[srcTokens.length - 1];

        /* Obtin calea catre sursa*/
        String[] theSourcePath = FSManager.getPathTo(srcTokens);

        /* Destinatia */
        String destPath = Argument.getArgument(2);
        String[] destTokens = destPath.split("/");
        String destFileName;
        String[] theDestPath;

        /* Obtin calea catre destinatie */
        if(destTokens.length > 0) {
            destFileName = destTokens[destTokens.length - 1];
            theDestPath = destTokens;
        }else {
            destFileName = "";
            theDestPath = new String[1];
            theDestPath[0] = "";
        }

        /* Ma mut in folderul din path-ul source */
        if(FSManager.goTo(theSourcePath) == 0) {
            /* Daca a reusit, continui executia */

            /* Ma mut temporar in folderul din path-ul sursei */
            FSFolder srcTemp = FSManager.getVirtualCurrent();

            FSNode nodeToCopy = null;

            /* Verific daca se copiaza tot folderul curent */
            if(srcPath.equals(".")) {
                nodeToCopy = FSManager.getCurrent();
            }else {
                /* Caut daca fisierul sau folderul exista in acest folder din path */
                nodeToCopy = srcTemp.findFile(srcFileName);
                if(nodeToCopy == null) {
                    /* Fisierul sau folderul de copiat nu exista */
                    Main.errors.write("mv: cannot move " + srcPath + ": No such file or directory\n");
                    return;
                }

            }

            /* Resetez folderul temporar la folderul curent pentru a nu avea erori
             * (sa nu se piarda folderul curent cand ma plimb temporar prin sistemul de fisiere) */
            FSManager.setVirtualCurrent(FSManager.getCurrent());

            /* Daca ajunge aici inseamna ca exista fisierul/folderul de copiat */
            /* Mut folderul curent temporar pe path-ul destinatiei */
            if (destFileName.equals("")) {
                /* Daca se ajunge aici inseamna ca folderul destinatie este root */
                FSManager.setVirtualCurrent(FSManager.getRoot());
            } else {
                if (FSManager.goTo(theDestPath) != 0) {
                    /* Daca mutarea nu s-a efectuat cu succes inseamna ca unul din foldere nu exista */
                    Main.errors.write("mv: cannot move into " + destPath + ": No such directory\n");
                    return;
                }
            }

            FSFolder destTemp = FSManager.getVirtualCurrent();

            /* Verific daca fisierul de copiat exista deja in destinatie */
            if(destTemp.findFile(nodeToCopy.getName()) != null) {
                Main.errors.write("mv: cannot move " + srcPath + ": Node exists at destination\n");
                return;
            }

            /* Sterg nodul din folderul initial care il contine */
            if(srcPath.equals(".")) {
                FSManager.getCurrent().getParent().remove(nodeToCopy);
            }else {
                srcTemp.remove(nodeToCopy);
            }
            nodeToCopy.setParent(destTemp);
            destTemp.add(nodeToCopy);

        }else {
            /* Daca nu a reusit, afisez eroarea si opresc comanda */
            Main.errors.write("mv: cannot move " + srcPath + ": No such file or directory\n");
            return;
        }

    }
}
