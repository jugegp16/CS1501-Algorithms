import java.util.Iterator;
import java.util.ArrayList;


public class PHPArray<V> implements Iterable<V> {
    private static final int INIT_CAPACITY = 4;
    private int N;
    private int M;
    private Node<V>[] entries;
    private Node<V> head;
    private Node<V> tail;
    private Node<V> cur;   

  // create an empty hash table - use 16 as default size
    public PHPArray() {
        this(INIT_CAPACITY);
    }

  // create a PHPArray of given capacity
    public PHPArray(int capacity) {
        M = capacity;
        @SuppressWarnings("unchecked")
        Node<V>[] temp = (Node<V>[]) new Node[M];
        entries = temp;
        head = tail = null;
        N = 0;
    }

    public Iterator<V> iterator() {
        return new MyIterator();
    }


    public void put(int key, V val) {
        put(Integer.toString(key),  val);
    }
    // insert the key-value pair into the symbol table
    public void put(String key, V val) {
        if (val == null) unset(key);

        // double table size if 50% full
        if (N >= M/2){
            System.out.println("\t\tsize: " + N + " -- Resizing from " + M + " to " +  M*2);
            resize(2*M);
        }

        // linear probing
        int i;
        //
        // at index within hash table, if collision, probe 
        //
        for (i = hash(key); entries[i] != null; i = (i + 1) % M) {
            // update the value if key already exists
            if (entries[i].key.equals(key)) {
                entries[i].value = val; return;
            }
        }
        //
        // found an empty entry
        //
        entries[i] = new Node<V>(key, val);
        //insert the node into the linked list
        // found an empty entry
        entries[i] = new Node<V>(key, val);
        if (head == null ){
            head = entries[i];
            tail = entries[i];
            cur = entries[i];
        } else {
            tail.next = entries[i];
            entries[i].prev = tail;
            tail = tail.next;
        }
        N++;
    }

    public V get(int key) {
        return get(Integer.toString(key));
    }

  // return the value associated with the given key, null if no such value
    public V get(String key) {
        for (int i = hash(key); entries[i] != null; i = (i + 1) % M){
            if (entries[i].key.equals(key)){
                return entries[i].value;
            }
        }
    return null;
    }

  // resize the hash table to the given capacity by re-hashing all of the keys
    private void resize(int capacity) {
        PHPArray<V> temp = new PHPArray<V>(capacity);
        //rehash the entries in the order of insertion
        Node<V> current = head;
        while(current != null){
            temp.put(current.key, current.value);
            current = current.next;
        }
        entries = temp.entries;
        head    = temp.head;
        tail    = temp.tail;
        cur     = temp.cur;
        M       = temp.M;
    }

  // rehash a node while keeping it in place in the linked list
    private void rehash(Node<V> node){
        System.out.println("\t\tKey " + node.key + " rehashed..." + '\n');
        int i;
        //
        // at index within hash table, if collision, probe 
        for (i = hash(node.key); entries[i] != null; i = (i + 1) % M) {
        }
        //
        // insert node into new spot 
        entries[i] = node; 
    }

    public void unset(int key) {
        unset(Integer.toString(key));
    }

  // delete the key (and associated value) from the symbol table
    public void unset(String key) {
        if (get(key) == null) return;

        // find position i of key
        int i = hash(key);
        //
        // linear probing til find it 
        while (!key.equals(entries[i].key)) {
            i = (i + 1) % M;
        }
        //
        // Delete node from hash table
        Node<V> toDelete = entries[i];
        entries[i] = null;
        //
        // one entry in List
        if (toDelete == head && toDelete == tail){
            head = null;
            tail = null; 
        //
        // delete head of List  
        } else if (toDelete == head){
            head = head.next;
            head.prev = null;
        //
        // delete tail of List
        } else if (toDelete == tail){
            toDelete = tail.prev;
            toDelete.next = null;
            tail = toDelete;
        //
        // normal case
        } else {
            toDelete = toDelete.prev;
            toDelete.next = toDelete.next.next;
            toDelete.next.prev = toDelete; 
        }
        //
        // rehash all keys in same cluster
        i = (i + 1) % M;
        while (entries[i] != null) {
            Node<V> nodeToRehash = entries[i];
            entries[i] = null;
            rehash(nodeToRehash);
            i = (i + 1) % M;
        }
        N--;
        // halves size of array if it's 12.5% full or less
        if (N > 0 && N <= M/8) resize(M/2);
    }

    // hash function for keys - returns value between 0 and M-1
    private int hash(String key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }
    // Return an ArrayList<String> of the keys, in order by the linked list
    public ArrayList<String> keys(){
        ArrayList<String> result = new ArrayList<String>();
        Node<V> temp = head;
        while (temp !=null){
            result.add(temp.key);
            temp = temp.next;
        }
        return result;
    }
    // Return an ArrayList<Integer> of the values,in order by the linked list
    public ArrayList<Integer> values(){
        ArrayList<Integer> result = new ArrayList<Integer>();
        Node<V> temp = head;
        while (temp !=null){
            result.add((int)temp.value);
            temp = temp.next;
        }
        return result;
    }
    // The each() method should return the next (key, value) pair within a new Pair
    // object until the end of the list is reached -- at which time it should return
    // null.
    public Pair<V> each(){
        Pair<V> result = null;
        if(cur == null){
            return null;
        }else {
            result = new Pair<V>(cur.key, cur.value); 
            cur = cur.next;
        }
        return result;
    }
    // reset() method will re-initialize the iteration such that each()
    // will again go through the (key, value) pairs of the PHPArray.
    public void reset(){
        cur = head;
    }
    // simple sort
    public void sort(){
        int counter=0;
        Node<V>[] nodes = (Node<V>[]) new Node[M];
        for (int i = 0; i < M; i++) {
            if (entries[i] !=null){
                nodes[counter++]=entries[i];
            }
        }
        quickSort(nodes, 0, N-1, 1);
        PHPArray<V> temp = new PHPArray<>(M);
        for(int i=0;i<N;i++) {
            temp.put(i,(V)nodes[i].value);
            entries[i]=nodes[i];
        }
        entries = temp.entries;
        head    = temp.head;
        tail    = temp.tail;
        cur     = temp.cur;
        M       = temp.M;
    }
    // sort and maintain keys 
    public void asort(){
        int counter=0;
        Node<V>[] nodes = (Node<V>[]) new Node[M];
        for (int i = 0; i < M; i++) {
            if (entries[i] !=null){
                nodes[counter++]=entries[i];
            }
        }
        quickSort(nodes, 0, N-1, 1);
        PHPArray<V> temp = new PHPArray<V>(M);
        for(int i=0;i<N;i++) {
            temp.put(nodes[i].key,(V)nodes[i].value);
            entries[i]=nodes[i];
        }
        entries = temp.entries;
        head    = temp.head;
        tail    = temp.tail;
        cur     = temp.cur;
        M       = temp.M;
    }
    
    // reverse sort
    public void rsort(){
        int counter=0;
        Node<V>[] nodes = (Node<V>[]) new Node[M];
        for (int i = 0; i < M; i++) {
            if (entries[i] !=null){
                nodes[counter++]=entries[i];
            }
        }
        quickSort(nodes, 0, N-1, -1);
        PHPArray<V> temp = new PHPArray<>(M);
        for(int i=0;i<N;i++) {
            temp.put(i,(V)nodes[i].value);
            entries[i]=nodes[i];
        }
        entries = temp.entries;
        head    = temp.head;
        tail    = temp.tail;
        cur     = temp.cur;
        M       = temp.M;
    }
    
    // reverse sort and maintain keys
    public void arsort(){
        int counter=0;
        Node<V>[] nodes = (Node<V>[]) new Node[M];
        for (int i = 0; i < M; i++) {
            if (entries[i] !=null){
                nodes[counter++]=entries[i];
            }
        }
        quickSort(nodes, 0, N-1, -1);
        PHPArray<V> temp = new PHPArray<V>(M);
        for(int i=0;i<N;i++) {
            temp.put(nodes[i].key,(V)nodes[i].value);
            entries[i]=nodes[i];
        }
        entries = temp.entries;
        head    = temp.head;
        tail    = temp.tail;
        cur     = temp.cur;
        M       = temp.M;
    }
    // Simple quicksort algorithm for array of nodes with the addition of
    // of an int representing "direction". direction (dir) shifts compareTo 
    // from < 0 to > 0 (*1 or *-1) allowing this sort to work in reverse 
    // order as well without any additional overheads.
    private void quickSort(Node<V>[] nodes, int low, int high, int dir){ 
        if (low < high){ 
            int partionIndex = partition(nodes, low, high, dir); 
            quickSort(nodes, low, partionIndex-1, dir); 
            quickSort(nodes, partionIndex+1, high, dir); 
        } 
    } 
    // quicksort helper method
    private int partition(Node<V>[] nodes, int low, int high, int dir){ 
        Node<V> pivot = nodes[high];  
        int i = low-1;
        for (int j=low; j<high; j++){ 
            // If current element is smaller than the pivot 
            if (dir*nodes[j].compareTo(pivot)<0){ 
                i++; 
                // swap 
                Node<V> temp = nodes[i]; 
                nodes[i] = nodes[j]; 
                nodes[j] = temp; 
            } 
        } 
        Node<V> temp = nodes[i+1]; 
        nodes[i+1] = nodes[high]; 
        nodes[high] = temp; 
        return i+1; 
    } 
    // return new PHPArray containing given keys / values arrays. 
    public PHPArray<V> array_combine(String[]keys, V[]values){
        PHPArray<V> result = new PHPArray<V>(M);
        for (int i=0;i<N;i++){
            put(keys[i],values[i]);
        }
        return result;
    }
    // return new PHPArray swapping each key and value pair with its corrisponding key or value. 
    public PHPArray<String> array_flip(){
        PHPArray<String> result = new PHPArray<String>(M);
        Node<V> temp = head;
        while(temp != null){
            if(!(temp.value instanceof String)){
                throw new ClassCastException("Cannot convert class java.lang.Integer to String");
            }
            result.put((String)temp.value, temp.key);
            temp = temp.next;
        }
        return result;
    }
    
    public int length(){
        return N;
    }
    // print raw contents of hashable
    public void showTable(){
        System.out.println("\t\tRaw Hash Table Contents");
        for (int i=0;i<M;i++){
            if (entries[i] != null){
                System.out.println(i + ": "+ "Key: " + entries[i].key + " Value: " +  entries[i].value);
            } else {
                System.out.println(i + ": null");
            }
        }
    }

    private class Node<V> implements Comparable<Node<V>>{
        private String key;
        private V value;
        private Node<V> next;
        private Node<V> prev;

        Node(String key, V value){
            this(key, value, null, null);
        }

        Node(String key, V value, Node<V> next, Node<V> prev){
            this.key = key;
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    
        @Override
        public int compareTo(Node<V> other) throws ClassCastException{
            return ((Comparable)this.value).compareTo(((Comparable)other.value));
        }
    }
    // The Pair<V> class (which you must write) should be a public static inner
	// class. This allows it to be nested within PHPArray but it can still be publicly
	// accessed as we are doing below.  Pair<V> has two public instance variables:
	// 		key of type String and
	// 		value of type V
    public static class Pair<V> {
        public String key;
        public V value;

        public Pair(String key, V value){
            this.key = key;
            this.value = value;
        }
    }

    public class MyIterator implements Iterator<V> {
        private Node<V> current;

        public MyIterator() {
            current = head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public V next() {
            V result = current.value;
            current = current.next;
        return result;
        }
    }
    
}
