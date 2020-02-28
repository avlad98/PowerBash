/**
 * Aceasta clasa reprezinta o componenta a design pattern-ului "Factory".
 */
public class CommandFactory {

    /**
     * Aceasta unica metoda intoarce o noua instanta de comanda pe baza tipului dorit dat ca parametru
     * @param tip
     * @return
     */
    public static Command getInstance(EnumCommands tip) {
        switch (tip){
            case ls:
                return new Ls();

            case pwd:
                return new Pwd();

            case cd:
                return new Cd();

            case cp:
                return new Cp();

            case mv:
                return new Mv();

            case rm:
                return new Rm();

            case touch:
                return new Touch();

            case mkdir:
                return new Mkdir();

            default:
                return null;
        }

    }

}
