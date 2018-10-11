/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vigenerecipher;

/**
 *
 * @author tians
 */
public class Main {
        public static void main(String[] args) {
        int[] key = {1,2,3};
        VigenereCipher c = new VigenereCipher(key);
        System.out.println(c);
    }
}
