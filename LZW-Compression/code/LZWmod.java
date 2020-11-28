/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
public class LZWmod {
    private static final int R = 256;   // number of input chars
    private static int W;               // number of codewords = 2^W
    private static int L;             // codeword width
    private static char reset;

public static void compress(){
        //
        // write reset flag
        //
        BinaryStdOut.write(reset); 
        L = 512;
        W = 9; 
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put(new StringBuilder("" + (char) i), i);

        int code = R+1;  
        StringBuilder currentStr = new StringBuilder();
        //
        // read first char
        //
        char c = BinaryStdIn.readChar();
        currentStr.append(c);
        int current_codeword=0;
        while (!BinaryStdIn.isEmpty()) {
            //
            // check if value is already in our symbol table
            //
            if(!st.contains(currentStr)){
                currentStr.deleteCharAt(currentStr.length() - 1);
                BinaryStdOut.write(st.get(currentStr), W);
                currentStr.append(c);
                //
                //  add to symbol table if not full
                // 
                if (code < 65536){
                    st.put(currentStr, code++);
                    if(code+1 == L){
                        if( W < 16){
                            W++; 
                            L*=2;
                        } else if (reset == '1' && W == 16){
                            //
                            // re-initialize symbol table + vars
                            //
                            st = new TST<Integer>();
                            for(int i = 0; i < R; i++) { 
                                st.put(new StringBuilder("" + (char) i), i);
                            }
                            L = 512; 
                            W = 9; 
                            code = R+1;
                        }
                    }
                }
            currentStr.delete(0, currentStr.length()-1);
            }
            //
            // read and append the next char to current
            //
            c = BinaryStdIn.readChar();
            currentStr.append(c);
        }
        // Write the codeword of whatever remains in current
        if (st.get(currentStr) != null)
            BinaryStdOut.write(st.get(currentStr),W);
        BinaryStdOut.write(R, W); //Write EOF
        BinaryStdOut.close();
    }

    public static void expand() {
        //
        // read reset flag
        //
        reset = BinaryStdIn.readChar();
        int L = 512; // number of codewords = 2^W
        int W = 9;  // codeword width
        String[] st = new String[65536];
        int i; // next available codeword value
        //
        // initialize symbol table with all 1-character strings
        //
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = ""; // (unused) lookahead for EOF
        //
        // write first value
        // 
        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];
        BinaryStdOut.write(val);
        //
        // Loop through remaining file
        // 
        while (true) {
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;

            BinaryStdOut.write(val);
            //
            // sync with compression
            //
            if (i+2 == L){
                if(W < 16) {
                    L*=2;
                    W++;
                }
            } else if (reset == '1' && W == 16){
                for (i = 0; i < R; i++)
                    st[i] = "" + (char) i;
                st[i++] = "";
                L = 512; 
                W = 9;
                i = R+1;
                //
                // re-write first value
                //
                codeword = BinaryStdIn.readInt(W);
                val = st[i];
                BinaryStdOut.write(val);
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        //
        // handle reset
        //
        if (args.length > 1 && args[1].equals("r")){
            reset = '1';
        } else {
            reset = '0';
        }
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}