package tester;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.duke.FileResource;
import vigenerecipher.*;
/**
 *
 * @author tians
 */
public class Tester {
    
    private String message;

    public Tester() {
        FileResource fr = new FileResource();
        message = fr.asString();
    }
    
    public void testCeasarCipher(){
        int key = 7;
        CaesarCipher cc = new CaesarCipher(key);
        String encrypted = cc.encrypt(message);
        System.out.println("The message: \n" + message);
        System.out.println("**********");
        System.out.println("Encrypted: \n" + encrypted);
    }
    
    public void testCeasarCracker(){
        CaesarCracker cc = new CaesarCracker('a');
        int key = cc.getKey(message);
        String decrypted = cc.decrypt(message);
        System.out.println("Input: \n" + message);
        System.out.println("***********");
        System.out.println("Key: " + key + "\nDecrypted: \n" + decrypted);
    }
    
    public void testVigenereCipher(){
        int[] key = {17,14,12,4};
        VigenereCipher vc = new VigenereCipher(key);
        String encrypted = vc.encrypt(message);
        System.out.println("The message: \n" + message);
        System.out.println("**********");
        System.out.println("Encrypted: \n" + encrypted);
    }
    
    public void testVigenereBreaker(){
        VigenereBreaker vb = new VigenereBreaker();
        vb.breakVigenere(message);
        
        
        /*
        int keyLength = 4;
        char mostCommon = 'e';
        vb.breakVigenere(message, keyLength, mostCommon);
        */
        /*
        String sliced = vb.sliceString("abcdefghijklm", 3, 4);
        System.out.println("abcdefghijklm sliced is: " + sliced);
        int keys[] = vb.tryKeyLength(message, 5, 'e');
        System.out.println("Keys: ");
        for(int i=0;i<keys.length;i++){
            System.out.print(keys[i] + ", ");
        }
        */
    }
}
