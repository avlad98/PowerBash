public interface FSNode extends Cloneable {
    public int add(FSNode node);
    public int remove(FSNode node);
    public void setParent(FSFolder parent);
    public FSNode getParent();
    public String getName();
    public String getPath();
    public String getParentPath();
    public String getType();
    public FSNode clone() throws CloneNotSupportedException;
    public void resetParents();
    boolean checkIfContainsCurrent();
    FSNode find(String name);
}