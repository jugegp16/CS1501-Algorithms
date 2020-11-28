import java.util.Random;

public class Substitute implements SymCipher {
    private byte[] byteArr;


    public Substitute(){
        byteArr = new byte[256];
        for(int i = 0; i < 256; i++){
            byteArr[i] = (byte)i;
        }
        //
        // swap mappings
        //
        Random r = new Random();
        byte temp;
        int ri;
        for (int i = 0; i < 256; i++)
		{
			ri = r.nextInt(256);
			temp = byteArr[i];
			byteArr[i] = byteArr[ri];
			byteArr[ri] = temp;
		}
    }

    public Substitute(byte [] byteArr){
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
            temp[i] = byteArr[temp[i + over*127] & 0xff];
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
        byte [] iByteArr = inverse();
        for(int i = 0; i < bytes.length; i++){
            bytes[i] = iByteArr[bytes[i + over*127] & 0xff];
            //
            // maintain bounds
            //
            if (i > 0 && i % 127 == 0){
                over--;
            }
        }
        return new String(bytes);
    }

    // generate inverse key set 
    public byte[] inverse(){
        byte [] temp = new byte[256];
        for(int i = 0; i < 256; i++){
			temp[byteArr[i] & 0xff] = (byte)i;
        }
        return temp;
    }

}