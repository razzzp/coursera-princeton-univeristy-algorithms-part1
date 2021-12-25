import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;

public class Deque <Item> implements Iterable <Item> {
   
    private Node _head;
    private Node _tail;
    private int _size = 0;

    private class Node {
        Node next;
        Node prev;
        Item item;

        Node(Item item){
            this.item = item;
        }

        Node (Item item, Node next, Node prev){
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    // construct an empty deque
    public Deque(){
    }

    // is the deque empty?
    public boolean isEmpty(){
        if (_head == null && _tail == null)
            return true;
        else
            return false;
    }

    // return the number of items on the deque
    public int size(){
        return _size;
    }

    // add the item to the front
    public void addFirst(Item item){
        if (item == null) throw new IllegalArgumentException("item is null");

        if (isEmpty()) {
            _head = new Node(item);
            _tail = _head;
        }
        else {
            // create new node with next pointing to current head
            //  and set as new head
            Node newNode = new Node(item, _head, null);
            _head.prev = newNode;
            _head = newNode;
        }
        _size++;
    }

    // add the item to the back
    public void addLast(Item item){
        if (item == null) throw new IllegalArgumentException("item is null");

        if (isEmpty()){
            _head = new Node(item);
            _tail = _head;
        } else {
            // add node to tail and set new tail
            _tail.next = new Node(item, null, _tail);
            _tail = _tail.next;
        }
        _size++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if (isEmpty()) throw new NoSuchElementException("list is empty");

        Item result  = _head.item;
        if (_head == _tail){
            // only one item
            _head = null;
            _tail = null;
        } else {
            // set new head to next item
            _head = _head.next;
            _head.prev = null;
        }
        return result;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if (isEmpty()) throw new NoSuchElementException("list is empty");

        Item result  = _tail.item;
        if (_head == _tail){
            // only one item
            _head = null;
            _tail = null;
        } else {
            // set new tail to prev
            _tail = _tail.prev;
            _tail.next = null;
        }
        return result;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{
        Node current = _head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item result = current.item;
            current = current.next;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }

    }

    private void _print(){
        StdOut.print('[');
        for (Item i : this){
            StdOut.print(i);
            StdOut.print(',');
        }
        StdOut.printf("]\n");
    }   

    // unit testing (required)
    public static void main(String[] args) {
        // test constructor
        StdOut.println("Construct Deque");
        Deque<Integer> intDeque = new Deque<>();
        intDeque._print();
        // test is empty
        StdOut.printf("isempty: %b\n", intDeque.isEmpty());

        // test add first
        intDeque.addFirst(1);
        intDeque._print();
        // test is empty
        StdOut.printf("isempty: %b\n", intDeque.isEmpty());
        
        intDeque.addFirst(2);
        intDeque._print();

        // test add last
        intDeque.addLast(11);
        intDeque.addLast(12);
        intDeque._print();

        // test add first
        intDeque.addFirst(1);
        intDeque._print();

        // test remove first
        StdOut.println(intDeque.removeFirst());
        StdOut.println(intDeque.removeFirst());
        intDeque._print();

        //test remove last
        StdOut.println(intDeque.removeLast());
        StdOut.println(intDeque.removeLast());
        StdOut.println(intDeque.removeLast());
        intDeque._print();
        StdOut.printf("isempty: %b\n", intDeque.isEmpty());

        // test remove empty
        try {StdOut.println(intDeque.removeFirst());}
        catch(NoSuchElementException e) {StdOut.println(e.getMessage());}
        try {StdOut.println(intDeque.removeLast());}
        catch(NoSuchElementException e) {StdOut.println(e.getMessage());}

        // test null add
        try {intDeque.addFirst(null);}
        catch(IllegalArgumentException e) {StdOut.println(e.getMessage());}
        try {intDeque.addLast(null);}
        catch(IllegalArgumentException e) {StdOut.println(e.getMessage());}

        // test iterator
        Deque<String> sDeque = new Deque<>();
        for (int i=0; i<10; i++){
            sDeque.addLast("lastitem " + i);
        }
        for (int i=0; i<10; i++){
            sDeque.addFirst("firstitem " + i);
        }

        for (String s : sDeque){
            StdOut.print(s+',');
        }
    }
}