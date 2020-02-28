import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Aceasta clasa reprezinta comanda "Ls"
 */
public class Ls implements Command {

    /**
     * Constructor gol folosit de CommandFactory pentru executarea unei comenzi de acest tip
     */
    public Ls() {
    }

    /**
     * Aceasta metoda executa comanda, iar daca scrierea in fisiere esueaza se va arunca exceptia
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        /* Iau lista de argumente si vad cate argumente sunt */
        LinkedList<String> argList = Argument.getArgs();
        if(argList.size() == 1) {
            /* Comanda este doar "ls" */
            /* Se va executa ls pe folderul curent */
            displayContent(FSManager.getCurrent());
        }else if(argList.size() == 2) {
            /* Comanda este de forma "ls -R" sau "ls <path>" */
            /* Verific care din cazuri este */
            String arg = argList.get(1);

            if(arg.charAt(0) == '-') {
                /* Comanda este "ls -R" */
                recursiveDisplay(FSManager.getCurrent());
                return;
            }

            /* Comanda este "ls <path>" */
            String[] tokens = arg.split("/");
            if(FSManager.goTo(tokens) != 0) {
                Main.errors.write("ls: " + arg + ": No such directory\n");
                return;
            }

            displayContent(FSManager.getVirtualCurrent());
        }else if(argList.size() == 3) {
            /* Comanda este de forma "ls -R <path>" sau "ls <path> -R" */
            /* Gasesc care din argumente este <path> si afisez recursiv continutul */
            String arg1 = argList.get(1);
            String arg2 = argList.get(2);
            String path;

            if(arg1.charAt(0) == '-') {
                /* Primul argument este "-R", deci al doilea este <path> */
                path = arg2;
            }else {
                /* Primul argument nu este "-R", deci este <path> */
                path = arg1;
            }

            /* Mut folderul temporar pe path-ul din comanda */
            String[] tokens = path.split("/");
            if(FSManager.goTo(tokens) != 0) {
                Main.errors.write("ls: " + arg2 + ": No such directory\n");
                return;
            }

            /* Afisez recursiv continutul din folderul temporar mutat pe <path> */
            recursiveDisplay(FSManager.getVirtualCurrent());
        }
    }

    /**
     * Aceasta metoda afiseaza folderul curent si continutul sau in fisierul de output sau scrie eroarea
     * corespunzatoare in fisierul "errors". Daca nu se reuseste scrierea in fisiere atunci se arunca o exceptie.
     * @param folder
     * @throws IOException
     */
    private void displayContent(FSFolder folder) throws IOException {
        Main.output.write(folder.getPath() + ":\n");

        String fileNames = "";
        boolean first = true;
        for(Map.Entry<String, FSNode> entry : folder.getContent().entrySet()) {
            FSNode file = entry.getValue();
            if(!first){
                fileNames += " ";
            }else {first = false; }

            fileNames += file.getPath();
        }

        Main.output.write(fileNames + "\n\n");
    }

    /**
     * Aceasta metoda afiseaza recursiv folderul curent si continutul sau impreuna cu continutul tuturor folderelor
     * din continutul acestui folder curent in fisierul de output sau scrie eroarea corespunzatoare in fisierul
     * "errors". Daca nu se reuseste scrierea in fisiere atunci se arunca o exceptie.
     * @param file
     * @throws IOException
     */
    private void recursiveDisplay(FSNode file) throws IOException {
        if(file instanceof FSFolder) {
            displayContent((FSFolder)file);
            TreeMap<String, FSNode> content = ((FSFolder) file).getContent();
            for(Map.Entry<String, FSNode> entry : content.entrySet()) {
                FSNode nextFile = entry.getValue();
                if(nextFile instanceof FSFolder){
                    recursiveDisplay(nextFile);
                }
            }
        }else {
            System.out.println("Eroare la afisare! " + file.getName() + " nu e folder");
        }
    }
}
