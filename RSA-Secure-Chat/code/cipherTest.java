  public class cipherTest{

	public static void main(String [] args){
	
	SymCipher Add = new Add128();
	SymCipher Sub = new Substitute();

	byte[] a = Add.encode("Hello There :)");
	byte[] b = Sub.encode("Goodbye Now :(");
	
	String c = Add.decode(a);
	String d = Sub.decode(b);
	
	System.out.println("" + c + "  " + d);
	
	}

}