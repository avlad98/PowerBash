/**
 * Aceasta clasa este o componenta a design pattern-ului "Composite" pentru crearea arborelui sistemului de fisiere.
 * Aceasta clasa reprezinta tipul "Fisier" care implementeaza un Nod (la general).
 * De asemenea, aceasta clasa implementeaza clonarea (utila la comanda "Cp")
 */
public class FSFile implements FSNode, Cloneable {

    /**
     * Fiecare fisier are un nume, un parinte si un tip (File sau Folder - ajuta la evitarea unor erori sau warning-uri)
     */
    private String name;
    private FSNode parent;
    private String type;

    /**
     * Contructor default pentru fisier - Primeste un nume si un parinte
     * @param name
     * @param parent
     */
    public FSFile(String name, FSNode parent) {
        this.name = name;
        this.parent = parent;
        this.type = "File";
    }

    /**
     * Aceasta metoda nu este folositoare in cadrul acestei clase
     * @param node
     * @return
     */
    @Override
    public int add(FSNode node) {
        return 1;
    }

    /**
     * Aceasta metoda nu este folositoare in cadrul acestei clase
     * @param node
     * @return
     */
    @Override
    public int remove(FSNode node) {
        return 1;
    }

    @Override
    public void setParent(FSFolder parent) {
        this.parent = parent;
    }

    @Override
    public FSNode getParent() {
        return this.parent;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Aceasta metoda intoarce un String reprezentand calea fisierului. Se foloseste de alta metoda recursiva
     * @return
     */
    @Override
    public String getPath() {
        String path = getParentPath() + "/" + name;

        return path;
    }

    /**
     * Aceasta metoda este ajutatoare pentru crearea path-ului sub forma de String
     * @return
     */
    @Override
    public String getParentPath() {
        String path = null;

        if(parent == FSManager.getRoot()) {
            path = parent.getParentPath() + parent.getName();
            return path;
        }

        if(parent == null){
            path = "";
        }else {
            path = parent.getParentPath() + "/" + parent.getName();
        }

        return path;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Aceasta metoda cloneaza fisierul, iar in caz de eroare arunca exceptia respectiva
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    public FSNode clone() throws CloneNotSupportedException{
        return (FSNode) super.clone();
    }

    /**
     * Aceasta metoda nu este folositoare in cadrul acestei clase
     */
    @Override
    public void resetParents() {
    }

    /**
     * Aceasta metoda nu este folositoare in cadrul acestei clase
     */
    @Override
    public boolean checkIfContainsCurrent() {
        return false;
    }

    /**
     * Aceasta metoda nu este folositoare in cadrul acestei clase
     */
    @Override
    public FSNode find(String name) {
        return null;
    }
}
