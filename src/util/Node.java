package util;

/**
 * <h3>Node</h3>
 * 
 * Class used by a {@link LinkedChain} for the lines
 * 
 * @author fisherjc
 * @version 13 Oct 2026
 * 
 * @param <T> The generic datatype argument to be stored in the Node
 */
public class Node<T> {
    private T data;
    private Node<T> next;

    /**
     * Create a Node with no next node
     * 
     * @param dataPortion The data to store
     */
    public Node(T dataPortion) {
        this(dataPortion, null);
    }

    /**
     * Create a node with a next node
     * 
     * @param dataPortion The data to store
     * @param nextNode    The next node in the chain
     */
    public Node(T dataPortion, Node<T> nextNode) {
        data = dataPortion;
        next = nextNode;
    }

    /**
     * Get the data
     * 
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * Get the next node
     * 
     * @return the next node (if any)
     */
    public Node<T> getNext() {
        return next;
    }

    /**
     * Set the next node
     * 
     * @param newNext the new next node
     */
    public void setNext(Node<T> newNext) {
        next = newNext;
    }
}