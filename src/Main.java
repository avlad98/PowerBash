import java.io.*;

/**
 * Aceasta clasa este cea care se executa (contine metoda "main")
 */
public class Main {
    /* Numar comanda */
    static int cmdIndex = 0;
    static BufferedWriter output;
    static BufferedWriter errors;

    public static void main(String[] args) {
        /* Se deschide fisierul de INPUT */
        BufferedReader input = null;
        try{
            input = new BufferedReader(new FileReader(args[0]));
        }catch(IOException e){
            System.out.println("Fisierul de input nu a fost deschis");
            System.exit(1);
        }

        /* Se deschide fisierul de OUTPUT */
        BufferedWriter output = null;
        try{
            output = new BufferedWriter(new FileWriter(args[1]));
        }catch(IOException e){
            System.out.println("Fisierul de output nu a fost deschis");
            System.exit(2);
        }
        Main.output = output;

        /* Se deschide fisierul de ERRORS */
        BufferedWriter errors = null;
        try{
            errors = new BufferedWriter(new FileWriter(args[2]));
        }catch(IOException e){
            System.out.println("Fisierul de erori nu a fost deschis");
            try{
                output.close();
            }catch(IOException e2) {
                System.out.println("Fisierul de output nu se poate inchide");
            }finally {
                System.exit(3);
            }
        }
        Main.errors = errors;

        /* Initializare sistem de fisiere */
        FSManager fileSystem = FSManager.getInstance();

        /* Initializare "Factory" de comenzi */
        CommandExec command = CommandExec.getInstance();
        Argument argument = Argument.getInstance();

        /* Parsare input */
        try{
            String line = null;
            while((line = input.readLine()) != null){
               String[] tokens = line.split(" ");
               String cmdName = tokens[0];

               /* Setez argumentele pentru comanda curenta */
               Argument.setArgs(tokens);

               /* Setez comanda curenta de executat in clasa care executa comenzile */
               command.setCommand(CommandFactory.getInstance(EnumCommands.valueOf(cmdName)));

               /* Scriu indexul comenzii curente in ambele fisiere (output si errors) */
               Main.writeCommandIndex();

               /* Resetez folderul temporar pentru a putea fi folosit de toate comenzile la fel */
               FSManager.resetVirtualCurrent();

               /* Execut comanda curenta indiferent de tipul acestuia */
               command.execute();
            }
        }catch (IOException e){
            System.out.println("Eroare in citirea din fisierul de input");
        }finally {
            try{
                output.close();
            }catch (IOException e2){
                System.out.println("Fisierul de output nu se poate inchide");
            }finally {
                try{
                    errors.close();
                }catch (IOException e3){
                    System.out.println("Fisierul de erori nu se poate inchide");
                }
            }
        }
    }

    /**
     * Aceasta metoda scrie in cele doua fisiere de output numaru comenzii curente
     * @throws IOException
     */
    public static void writeCommandIndex() throws IOException {
        ++(Main.cmdIndex);
        output.write((Main.cmdIndex) + "\n");
        errors.write((Main.cmdIndex) + "\n");
    }
}
