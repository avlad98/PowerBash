import java.util.Arrays;

/**
 * Aceasta clasa face parte din design pattern-ul "Composite" reprezentand managerul tuturor instantelor de nod (fisiere
 * sau foldere).
 * De asemenea aceasta clasa contine si folderul principal "root" impreuna cu folderul curent. Pe langa acestea mai este
 * si un folder temporar "virtualCurrent" cu care navighez prin sistemul de fisiere fara a "strica" ceva.
 * Aceasta clasa modeleaza pattern-ul "Singleton" deoarece nu trebuie create mai multe sisteme de fisiere in cadrul
 * acestei teme. (trebuie sa existe decat un singur "root")
 */
public class FSManager {
    private volatile static FSManager instance;
    private static FSFolder current;
    private static FSFolder root;
    private static FSFolder virtualCurrent;

    /**
     * Constructor simplu care initializeaza un folder root si seteaza initial folderul curent la root (impreuna cu
     * folderul temporar).
     * @param root
     */
    private FSManager(FSFolder root) {
        FSManager.root = root;
        FSManager.current = root;
        FSManager.virtualCurrent = current;
    }

    /**
     * Implementare Singleton pentru sistemul de fisiere
     * @return
     */
    public static FSManager getInstance() {
        if(instance == null) {
            /* Thread safe */
            synchronized (FSManager.class) {
                if(instance == null) {
                    instance = new FSManager(new FSFolder("", null));
                }
            }
        }

        return instance;
    }

    public static FSFolder getRoot() {
        return root;
    }

    public static FSFolder getCurrent() {
        return current;
    }

    /**
     * Atunci cand se seteaza noul folder curent se va reseta si folderul temporar
     * @param current
     */
    public static void setCurrent(FSFolder current) {
        FSManager.current = current;
        FSManager.setVirtualCurrent(current);
    }

    public static FSFolder getVirtualCurrent() { return virtualCurrent; }

    public static void setVirtualCurrent(FSFolder folder) { FSManager.virtualCurrent = folder; }

    public static void resetVirtualCurrent() {
        FSManager.setVirtualCurrent(FSManager.getCurrent());
    }

    public static String getCurrentPath() {
        return current.getPath();
    }

    /**
     * Aceasta metoda seteaza folderul temporar (virtualCurrent) cu un pas inapoi (va urca in ierarhia de fisiere).
     * Se va intoarce true daca a reusit urcarea si false daca se incearca urcarea mai sus de "root"
     * @return
     */
    public static boolean stepBack() {
        FSManager.virtualCurrent = (FSFolder) FSManager.virtualCurrent.getParent();
        if(FSManager.virtualCurrent == null) {
            return false;
        }

        return true;
    }

    /**
     * Aceasta metoda primeste o cale sub forma de array de String-uri si muta folderul temporar la folderul specificat
     * de calea data ca parametru.
     * Se va intoarce 0 daca s-a reusit traversarea si 1 daca unul din folderele din cale nu exista
     * @param tokens
     * @return
     */
    public static int goTo(String[] tokens) {
        /* Initial se porneste din root */
        FSFolder temp = FSManager.getRoot();

        /* Daca calea este vida inseamna ca se doreste plecarea din "root" */
        if(tokens.length == 0) {
            FSManager.setVirtualCurrent(FSManager.getRoot());
            return 0;
        }

        /* Se verifica fiecare token din path-ul dat ca parametru si se navigheaza corespunzator */
        for(int i = 0; i < tokens.length; i++) {
            /* Daca token-ul este "" inseamna ca se porneste din "root" */
            if(tokens[i].equals("")) {
                FSManager.setVirtualCurrent(FSManager.getRoot());
            }else if(tokens[i].equals("..")) {
                /* Daca token-ul este ".." folderul temporar urca un pas in ierarhia de fisiere (comanda fiind
                 * verificata pentru succes sau failure */
                if(FSManager.stepBack()) {
                    /* Si atat deocamdata */
                }else {
                    return 1;
                }
            }else if(tokens[i].equals(".")) {
                /* Nu se intampla nimic
                 * VirtualCurrent ramane pe loc */
            }else {
                /* Daca token-ul curent reprezinta un nume atunci se cauta in folderul temporar nodul.
                 * Daca nodul nu exista se intoarce 1 (failure).
                 * Daca nodul nu este folder se va intoarce de asemenea 1 (failure)*/
                temp = FSManager.getVirtualCurrent();
                FSNode nextNode = temp.findFile(tokens[i]);
                FSFolder nextFolder;

                if(nextNode != null && nextNode.getType().equals("Folder")) {
                    nextFolder = (FSFolder)nextNode;
                }else {
                    return 1;
                }

                /* Dupa succes se va muta folderul temporar pe folderul gasit */
                FSManager.setVirtualCurrent(nextFolder);
            }
        }

        return 0;
    }

    /**
     * Aceasta metoda primeste ca argument un path impartit in token-uri (array de String-uri) si intoarce
     * tot un array de String-uri care reprezinta calea catre ultimul nod din path. (Se "sterge" ultimul token din path)
     * @param tokens
     * @return
     */
    public static String[] getPathTo (String[] tokens) {
        if(tokens.length > 1) {
            return Arrays.copyOf(tokens, tokens.length-1);
        }

        /* Daca path-ul contine unul sau niciun token inseamna ca se doreste folderul curent si intorc simbolul pentru
         * folderul curent "." */
        String[] ret = {"."};
        return ret;
    }

    /**
     * Aceasta metoda primeste un path structurat in token-uri si intoarce un path pentru a fi afisat in fisierul de
     * erori.
     * @param tokens
     * @return
     */
    public static String getErrorMsg(String[] tokens) {
        String errPath = "";
        boolean first = true;
        for(int i = 0; i < tokens.length; i++) {
            if(!first) {
                errPath += "/";
            }else {
               first = false;
            }

            errPath += tokens[i];
        }

        return errPath;
    }

}