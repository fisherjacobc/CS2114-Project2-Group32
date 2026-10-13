package util;

/**
 * <h3>LinkedChain</h3>
 * 
 * Class utilized by {@link Node}s for the lines
 * 
 * @author fisherjc
 * @version 13 Oct 2026
 * 
 * @param <T> The generic datatype argument to be stored in the Node(s)
 */
public class LinkedChain<T> {
    private Node<T> head;
    private int numberOfEntries;

    /**
     * Create a new linked chain
     */
    public LinkedChain() {
        head = null;
        numberOfEntries = 0;
    }

    /**
     * Get the current size
     * 
     * @return the number of entries
     */
    public int getCurrentSize() {
        return numberOfEntries;
    }

    /**
     * Returns true if this linked chain contains anEntry, using equals() to
     * compare.
     * Provided so that tests can verify addElement without needing any
     * other linked chain methods.
     * 
     * @return if the entry is contained in the linked chain
     */
    public boolean contains(T anEntry) {
        boolean found = false;
        Node<T> currentNode = head;
        while (!found && (currentNode != null)) {
            if (currentNode.getData().equals(anEntry)) {
                found = true;
            }
            currentNode = currentNode.getNext();
        }
        return found;
    }

    /**
     * Adds a new entry to this linked chain.
     *
     * @param anEntry the entry to add
     * @return true if anEntry was added successfully, or false if anEntry
     *         is null and cannot be added
     */
    public boolean add(T anEntry) {
        if (anEntry == null) {
            return false;
        }

        Node<T> newEntry = new Node<>(anEntry);

        newEntry.setNext(head);
        head = newEntry;
        numberOfEntries++;

        return true;
    }

    /**
     * Get the last node
     * 
     * @return a reference to the last node in the chain, or null if the
     *         chain is empty
     */
    public Node<T> getLastNode() {
        Node<T> curr = head;

        if (curr == null) {
            return null;
        }

        while (curr.getNext() != null) {
            curr = curr.getNext();
        }

        return curr;
    }

    /**
     * @param value the value to search for
     * @return a reference to the first node whose data equals value, or
     *         null if no such node exists (or the chain is empty)
     */
    public Node<T> getFirstReferenceToValue(T value) {
        if (head == null || value == null) {
            return null;
        }

        Node<T> curr = head;
        Node<T> found = null;
        while (curr != null) {
            if (curr.getData().equals(value)) {
                found = curr;
                break;
            }

            curr = curr.getNext();
        }

        return found;
    }

    /**
     * Removes the given node from the chain. If desiredNode is null or is
     * not found in the chain, the chain is left unchanged.
     *
     * @param desiredNode the node to remove
     * @return the data inside the removed node
     */
    public T remove(Node<T> desiredNode) {
        if (desiredNode == null || head == null) {
            return null;
        }

        if (head == desiredNode) {
            head = head.getNext();
            numberOfEntries--;

            return desiredNode.getData();
        }

        Node<T> currentNode = head;

        while (currentNode != null && currentNode.getNext() != desiredNode) {
            currentNode = currentNode.getNext();
        }

        if (currentNode != null) {
            currentNode.setNext(desiredNode.getNext());
            numberOfEntries--;

            return desiredNode.getData();
        }

        return null;
    }

    /**
     * Removes all nodes from this linked chain, leaving it empty.
     */
    public void removeAll() {
        head = null;
        numberOfEntries = 0;
    }

    /**
     * Returns a string representation of this linked chain's contents, in the order
     * they appear in the chain, formatted like "[A,B,C]". An empty linked chain
     * returns "".
     */
    @Override
    public String toString() {
        if (head == null) {
            return "";
        }

        String representation = "[";
        representation += head.getData().toString();

        Node<T> curr = head.getNext();
        while (curr != null) {
            representation += "," + curr.getData();

            curr = curr.getNext();
        }

        representation += "]";

        return representation;
    }
}