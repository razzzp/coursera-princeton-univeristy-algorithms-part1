import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] _itemArray;
    private int _arraySize;
    private int _itemCount = 0;
    private int _tail = 0;

    // construct an empty randomized queue
    public RandomizedQueue(){
        _arraySize = 1;
        _itemArray =  (Item[]) new Object[_arraySize];
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return _itemCount == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return _itemCount;
    }

    private void _resize(int newSize){
        int idxInCopy=0;
        Item[] copy = (Item[]) new Object[newSize];
        // ensure array copy is contiguous
        for (int i=0;i<_arraySize;i++){
            if (_itemArray[i]!=null) copy[idxInCopy++] = _itemArray[i];
        }
        _tail = _itemCount;
        _arraySize = newSize;
        _itemArray = copy;
    }

    // add the item
    public void enqueue(Item item){
        if (item==null) throw new IllegalArgumentException("item is null");

        if (_tail == _arraySize) _resize(_arraySize*2);
        _itemArray[_tail++] = item;
        _itemCount++;
    }

    // remove and return a random item
    public Item dequeue(){
        if (isEmpty()) throw new NoSuchElementException("list is empty");

        // how bad is this? at worst 1/4 chance elem is not nil, O(constant)?
        Item result = null;
        int rnd = 0;
        while (result == null){
            rnd = StdRandom.uniform(_arraySize);
            result = _itemArray[rnd];
        }
        // how to remove?
        //  set to null and let resize compact the list
        _itemArray[rnd] = null;
        if (--_itemCount <= _arraySize/4) _resize(_arraySize/2);

        return result;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if (isEmpty()) throw new NoSuchElementException("list is empty");

        Item result = null;
        while (result == null){
            int rnd = StdRandom.uniform(_arraySize);
            result = _itemArray[rnd];
        }
        return result;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        _resize(_arraySize);
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item>{
        int[] _indexOrder;
        int _curIndex = 0;
        final int _iterSize;

        RandomizedQueueIterator(){
            // resize so list compacts o(n)
            _resize(_arraySize);
            // generate list with index order o(n)
            // use iter size in case collection modified
            _iterSize = _itemCount;
            _indexOrder = new int[_iterSize];
            for (int i =0; i< _iterSize;i++){
                _indexOrder[i] = i;
            }
            StdRandom.shuffle(_indexOrder);
        }

        @Override
        public boolean hasNext() {
            return _curIndex < _iterSize;
        }

        @Override
        public Item next() {
            return _itemArray[_indexOrder[_curIndex++]];
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }
    }

    private void _print(){
        StdOut.print('[');
        for (int i=0;i<_arraySize;i++){
            StdOut.print(_itemArray[i]);
            StdOut.print(',');
        }
        StdOut.printf("]\n");
    }  

    // unit testing (required)
    public static void main(String[] args){
        // test constructor
        StdOut.println("Construct RandomizedQueue");
        RandomizedQueue<Integer> intRandQueue = new RandomizedQueue<>();
        intRandQueue._print();
        // test is empty
        StdOut.printf("isempty: %b\n", intRandQueue.isEmpty());

        // test add first
        intRandQueue.enqueue(1);
        intRandQueue._print();
        // test is empty
        StdOut.printf("isempty: %b\n", intRandQueue.isEmpty());
        
        intRandQueue.enqueue(2);
        intRandQueue._print();

        // test add last
        intRandQueue.enqueue(11);
        intRandQueue.enqueue(12);
        intRandQueue._print();

        // test add first
        intRandQueue.enqueue(1);
        intRandQueue._print();

        // test remove first
        StdOut.println(intRandQueue.dequeue());
        StdOut.println(intRandQueue.dequeue());
        intRandQueue._print();

        //test remove last
        StdOut.println(intRandQueue.dequeue());
        StdOut.println(intRandQueue.dequeue());
        StdOut.println(intRandQueue.dequeue());
        intRandQueue._print();
        StdOut.printf("isempty: %b\n", intRandQueue.isEmpty());

        // test remove empty
        try {StdOut.println(intRandQueue.dequeue());}
        catch(NoSuchElementException e) {StdOut.println(e.getMessage());}
        try {StdOut.println(intRandQueue.dequeue());}
        catch(NoSuchElementException e) {StdOut.println(e.getMessage());}

        // test null add
        try {intRandQueue.enqueue(null);}
        catch(IllegalArgumentException e) {StdOut.println(e.getMessage());}
        try {intRandQueue.enqueue(null);}
        catch(IllegalArgumentException e) {StdOut.println(e.getMessage());}

        // test iterator
        RandomizedQueue<String> sDeque = new RandomizedQueue<>();
        for (int i=0; i<10; i++){
            sDeque.enqueue("lastitem " + i);
        }
        for (int i=0; i<10; i++){
            sDeque.enqueue("firstitem " + i);
        }

        for (String s : sDeque){
            StdOut.print(s+',');
        }
    }

}