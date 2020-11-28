import java.util.Random;

public class Add128 implements SymCipher{
    private byte[] byteArr;

    public Add128(){
        byteArr = new byte[128];
        Random r = new Random();
        r.nextBytes(byteArr);
    }

    public Add128(byte [] byteArr){
        this.byteArr = byteArr;
    }
    // Return an array of bytes that represent the key for the cipher
	public byte [] getKey(){
        return byteArr;
    }

	// Encode the string using the key and return the result as an array of bytes. 
	public byte [] encode(String S){
        byte [] temp = S.getBytes();
        int over = 0;
        for(int i = 0; i < temp.length; i++){
            temp[i] += byteArr[i + over*127];
            //
            // maintain bounds
            //
            if (i > 0 && i % 127 == 0){
                over--;
            }
        }
        return temp;
    }

	// Decrypt the array of bytes and generate and return the corresponding String.
	public String decode(byte [] bytes){
        int over = 0;
        for(int i = 0; i < bytes.length; i++){
            bytes[i] -= byteArr[i + over*127];
            //
            // maintain bounds
            //
            if (i > 0 && i % 127 == 0){
                over--;
            }
        }
        return new String(bytes);
    }
}
