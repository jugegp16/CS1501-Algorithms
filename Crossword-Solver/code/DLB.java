public class DLB<E> implements DictInterface {
    private static final char SENTINEL = '^';
    private Node root;

    /** Add a new String to the DictInterface
	 * @param s the string to be added
	 * @return true if the string was added successfully; false otherwise
	 */
    public boolean add(String s){
        s = s + SENTINEL;
        root = add(root, s, 0);
        if(root == null){
            return false;
        }
        return true;
    }

    /** Same logic as method above. However, now we are passing in node(x)
	 * as an additional arguement to represent the current node.
     * @param x current node 
	 * @param s 
	 * @return 
	 */
    private Node add(Node x, String s, int pos){
        //
        // NODE DNE
        //   	
        Node result = x;
        if (x == null){
            result = new Node();
            result.letter = s.charAt(pos);
            if(pos < s.length()-1){   
                result.child = add(x, s, pos+1);
            }
        //
        // LETTER THERE
        //   	
        } else if (x.letter == s.charAt(pos)){
            if(pos < s.length()-1){
                result.child  = add(x.child, s, pos+1);
            } 
        //
        // LETTER NOT THERE
        //	
        } else {
            result.sibling = add(x.sibling, s, pos);
        }
        return result;
    }

    /* @param s the string to be searched for
    * @return 0 if s is not a word or prefix within the DictInterface
    * 	                  1 if s is a prefix within the DictInterface but not a 
    *                       valid word
    *                    2 if s is a word within the DictInterface but not a
    *                        prefix to other words
    *                    3 if s is both a word within the DictInterface and a
    *                        prefix to other words
    */   
    public int searchPrefix(StringBuilder s){
        return searchPrefix(s, 0, s.length()-1);
    }

    /** Same logic as method above.  However, now we can search a substring
	 * from start (inclusive) to end (inclusive) within the StringBuilder.
	 * Depending on how you implement your main search algorithm, you may
	 * find this version to be more convenient or appropriate than the first
	 * one above.
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
    public int searchPrefix(StringBuilder s, int start, int end){
        return searchPrefix(root, s, start, end);
    }

     /** Same logic as method above. However, now we are passing in node(x)
	 * as an additional arguement to represent the current node.
     * @param x current node
	 * @param s 
	 * @param start
	 * @param end
	 * @return
	 */
    private int searchPrefix(Node x, StringBuilder s, int start, int end){
        boolean isPrefix = false;
        boolean isWord = false;
        if (x !=null) {
            //
            // LETTERS MATCH 
            //
            if ( x.letter == s.charAt(start)){
                //
                // END OF STRING  -----> CHECK IF WORD / PREFIX
                //
                if(start == end){
                    Node cur;
                    if (x.child != null){
                        cur = x.child;
                        while (cur != null){
                            if (cur.letter == SENTINEL){
                                isWord = true;
                            }else if (cur.letter != SENTINEL ){
                                isPrefix = true;
                            }
                            cur = cur.sibling;
                        }
                    }
                //
                // NOT END OF STRING ---> RECURSE ON CHILD
                //
                } else {
                    return searchPrefix(x.child, s, start + 1, end);
                } 
            //
            // LETTERS DONT MATCH 
            //
            } else {
                return searchPrefix(x.sibling, s, start, end);     
            }
            if (isPrefix && isWord) return 3;
            else if (isWord) return 2;
            else if (isPrefix) return 1;
            else return 0;         
        }
        return 0;
    }
    private class Node {
        private char letter;
        private Node sibling;
        private Node child;
    }
}