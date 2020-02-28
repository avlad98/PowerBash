import java.io.IOException;

/**
 * Aceasta clasa este creata dupa design pattern-urile "Singleton" si "Command".
 * Instanta este creata o singura data.
 * Contine o comanda la un moment de timp care este setata prin metoda "setCommand" si
 * executata prin metoda "execute" ca restul comenzilor.
 */
public class CommandExec implements Command {
    private static CommandExec instance = null;
    private Command command;

    /**
     * Constructor gol -> Este folosit doar la instantierea singleton-ului
     */
    public CommandExec() {
    }

    /**
     * (Componenta a Singleton-ului)
     * @return Intoarce instanta creata sau deja creata anterior
     */
    public static CommandExec getInstance() {
        if(instance == null) {
            /* Thread safe */
            synchronized (CommandExec.class) {
                if(instance == null) {
                    instance = new CommandExec();
                }
            }
        }

        return instance;
    }

    /**
     * Aceasta metoda primeste o comanda si o seteaza in cadrul acestei clase pentru a
     * putea fi executata la un moment de timp
     * @param command
     */
    public void setCommand(Command command) { this.command = command; }

    /**
     * Aceasta metoda executa comanda curenta din aceasta clasa.
     * Intoarce o exceptie atunci cand nu reuseste scrierea in fisiere
     * @throws IOException
     */
    @Override
    public void execute() throws IOException {
        command.execute();
    }
}
