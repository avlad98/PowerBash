import java.io.IOException;
import java.util.LinkedList;

/**
 * Aceasta clasa reprezinta comanda "Cp"
 */
public class Cp implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Cp() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
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

        /* Obtin calea catre sursa si destinatie */
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
                    Main.errors.write("cp: cannot copy " + srcPath + ": No such file or directory\n");
                    return;
                }

            }

            /* Resetez folderul temporar la folderul curent */
            FSManager.setVirtualCurrent(FSManager.getCurrent());

            /* Daca ajunge aici inseamna ca exista fisierul/folderul de copiat */
            /* Mut folderul curent temporar pe path-ul destinatiei */
            if(!destFileName.equals("")) {
                if (FSManager.goTo(theDestPath) != 0) {
                    /* Daca mutarea nu s-a efectuat cu succes inseamna ca unul din foldere nu exista */
                    Main.errors.write("cp: cannot copy into " + destPath + ": No such directory\n");
                    return;
                }
            }else {
                /* Daca se ajunge aici inseamna ca folderul destinatie este root */
                FSManager.setVirtualCurrent(FSManager.getRoot());
            }

            FSFolder destTemp = FSManager.getVirtualCurrent();

            /* Verific daca fisierul de copiat exista deja in destinatie */
            if(destTemp.findFile(nodeToCopy.getName()) != null) {
                Main.errors.write("cp: cannot copy " + srcPath + ": Node exists at destination\n");
                return;
            }

            FSNode fileToCopy = null;
            try {
                if(nodeToCopy.getType().equals("Folder")) {
                    /* Se va copia folderul impreuna cu restul folderelor din continutul acestuia */
                    fileToCopy = nodeToCopy.clone();
                    fileToCopy.setParent(destTemp);
                    fileToCopy.resetParents();
                    if(destTemp.add(fileToCopy) != 0) {
                        System.out.println("Eroare la copiere in destinatie. Deja exista");
                    }
                }else {
                    /* Se va copia fisierul */
                    fileToCopy = nodeToCopy.clone();
                    /* Setez ca parinte noul folder pentru fisierul de copiat */
                    fileToCopy.setParent(destTemp);
                    if(destTemp.add(fileToCopy) != 0) {
                        System.out.println("Eroare la copiere in destinatie. Deja exista");
                    }
                }
            } catch (CloneNotSupportedException e) {
                System.out.println("Eroare la clonarea " + srcPath);
                e.printStackTrace();
            }

        }else {
            /* Daca nu a reusit, afisez eroarea si opresc comanda */
            Main.errors.write("cp: cannot copy " + srcPath + ": No such file or directory\n");
            return;
        }
    }
}
