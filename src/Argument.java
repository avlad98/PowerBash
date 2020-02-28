import java.util.LinkedList;

/**
 * Aceasta clasa retine intr-o lista argumentele comenzii curente.
 * Am ales aplicarea design pattern-ului Singleton pentru aceasta clasa deoarece nu doresc mai multe instante pentru ca
 * aceasta lista se modifica la fiecare comanda
 *
 * instance -> instanta pentru Singleton
 * args -> Lista inlantuita care contine argumentele comenzii
 */
public class Argument {
    private volatile static Argument instance;
    private static LinkedList<String> args;

    /**
     * Constructor gol (nu este nevoie de nicio initializare aici)
     */
    public Argument() {

    }

    /**
     * @return Intoarce instanta acestei clase
     */
    public static Argument getInstance() {
        if(instance == null) {
            /* Thread safe */
            synchronized (Argument.class) {
                if(instance == null) {
                    instance = new Argument();
                }
            }
        }

        return instance;
    }

    /**
     *
     * @return Intoarce lista de argumente
     */
    public static LinkedList<String> getArgs() {
        return args;
    }

    /**
     * Primeste un array de string-uri reprezentand argumentele care se vor stoca in lista acestei clase
     * @param tokens
     */
    public static void setArgs(String[] tokens) {
        LinkedList<String> tmp = new LinkedList<String>();
        for(String token : tokens) {
            tmp.add(token);
        }

        args = tmp;
    }

    /**
     * @param i
     * @return Intoarce argumentul de pe pozitia i
     *
     * In lista, primul argument este numele comenzii (nu este folosit)
     */
    public static String getArgument(int i) {
        if(i >= args.size()){
            System.out.println("Eroare! Nu exista atatea argumente");
            return null;
        }

        return args.get(i);
    }
}
