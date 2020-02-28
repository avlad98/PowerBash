import java.util.Map;
import java.util.TreeMap;

/**
 * Aceasta clasa este o componenta a design pattern-ului "Composite" pentru crearea arborelui sistemului de fisiere.
 * Aceasta clasa reprezinta tipul "Folder" care extinde "File" (Am luat in considerare citatul UNIX:
 * "Everything is a file".
 * De asemenea, aceasta clasa implementeaza clonarea (utila la comanda "Cp")
 */
public class FSFolder extends FSFile implements Cloneable {
    /**
     * Am ales TreeMap-ul ca structura de date care imi retine continutul folderului (deoarece tine lista sortata alfabetic)
     */
    private TreeMap<String,FSNode> content;

    /**
     * Constructorul care construieste aceasta instanta ca un "File", dar initializeaza si Tree-ul care reprezinta
     * continutul folderului. De asemenea seteaza si tipul acestuia la "Folder".
     * @param name
     * @param parent
     */
    public FSFolder(String name, FSNode parent) {
        super(name, parent);
        content = new TreeMap<String, FSNode>();
        setType("Folder");
    }

    public TreeMap<String,FSNode> getContent() {
        return content;
    }

    /**
     * Aceasta metoda adauga un nod in TreeMap.
     * Daca nodul exista deja, metoda intoarce 1 (fail).
     * Altfel, se insereaza elementul in TreeMap si se intoarce 0 (succes)
     * @param node
     * @return
     */
    @Override
    public int add(FSNode node) {
        FSNode tmp = findFile(node.getName());

        if(tmp != null) {
            /* Fisierul exista deja */
            /* Nu se va adauga */
            return 1;
        }

        content.put(node.getName(), node);
        return 0;
    }

    /**
     * Aceasta metoda sterge un nod din TreeMap
     * Daca nodul nu exista, metoda intoarce 1 (fail).
     * Altfel, se sterge elementul din TreeMap si se intoarce 0 (succes).
     * @param node
     * @return
     */
    @Override
    public int remove(FSNode node) {
        FSNode tmp = findFile(node.getName());

        if(tmp == null) {
            /* Fisierul nu exista, deci nu se va sterge nimic */
            return 1;
        }

        content.remove(node.getName());
        return 0;
    }

    /**
     * Aceasta metoda cauta un fisier sau folder dupa nume in continut si ii intoarce referinta.
     * Daca nu a fost gasit intoarce "null".
     * @param name
     * @return
     */
    public FSNode findFile(String name) {
        for (Map.Entry<String, FSNode> entry : content.entrySet()) {
            FSNode value = entry.getValue();
            String key = entry.getKey();

            if(key.equals(name)){
                return value;
            }
        }

        return null;
    }

    /**
     * Aceasta metoda reseteaza parintii recursiv (inclusiv parintii folderelor din continutul acestui folder)
     */
    public void resetParents() {
        for(Map.Entry<String, FSNode> entry : content.entrySet()) {
            FSNode value = entry.getValue();
            value.setParent(this);
            if(value.getType().equals("Folder")) {
                value.resetParents();
            }
        }
    }

    /**
     * Aceasta metoda face "deep clone". (Cloneaza si TreeMap-ul inclusiv elementele din TreeMap recursiv).
     * Se va arunca o exceptie daca nu s-a reusit clonarea.
     * @return
     * @throws CloneNotSupportedException
     */
    public FSNode clone() throws CloneNotSupportedException {
        FSFolder clone = (FSFolder) super.clone();
        TreeMap<String,FSNode> cloneContent = (TreeMap<String, FSNode>) this.content.clone();

        for(Map.Entry<String, FSNode> entry : cloneContent.entrySet()) {
            FSNode value = entry.getValue();
            String key = entry.getKey();
            FSNode cloneValue;

            cloneValue = value.clone();
            cloneValue.setParent(this);
            cloneContent.replace(key, cloneValue);
        }

        clone.content = cloneContent;

        return clone;
    }

    /**
     * Aceasta metoda verifica daca folderul curent exista in continutul acestui folder sau este chiar acest folder.
     * Intoarce "true" daca este sau contine folderul curent si "false" altfel
     * @return
     */
    @Override
    public boolean checkIfContainsCurrent() {
        boolean contains = false;

        for(Map.Entry<String, FSNode> entry : content.entrySet()) {
            FSNode value = entry.getValue();
            if(value.getType().equals("Folder") && value == FSManager.getCurrent()) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * Aceasta metoda cauta recursiv in folder un fisier sau folder dupa nume si intoarce referinta ca Nod daca este gasit.
     * @param name
     * @return
     */
    public FSNode find(String name) {
        FSNode maybeThis = null;

        for(Map.Entry<String, FSNode> entry : content.entrySet()) {
            FSNode value = entry.getValue();
            if(value.getType().equals("Folder")) {
                if(value.getName().equals(name)) {
                    return value;
                }else{
                    maybeThis = value.find(name);
                }

                if(maybeThis != null) {
                    return maybeThis;
                }
            }
        }

        return null;
    }
}
