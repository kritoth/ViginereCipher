package vigenerecipher;

import java.util.*;
import edu.duke.*;
import java.io.File;

public class VigenereBreaker {
    /*
    * returns a String consisting of every totalSlices-th character from message,
    * starting at the whichSlice-th character
    */
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder sliced = new StringBuilder();
        for(int i = whichSlice; i<message.length(); i += totalSlices){
            sliced.append(message.charAt(i));
        }
        return sliced.toString();
    }

    /*
    * Returns the keys for the @param encrypted String by slicing it with @method sliceString()
    * and in each slice finds the key by using @method getKey() of @class CaesarCracker
    * the keys of each sliced String are stored into an int array and returned.
    */
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i=0; i<key.length; i++){
            String sliced = sliceString(encrypted, i, klength);
            key[i] = cc.getKey(sliced);
        }
        return key;
    }

    /*
    * This is called to decrypt the input @param encrypted, by calling the @link breakForAllLangs()
    * method and passing a HashMap of language-dictionary pairs as Key-Value.
    * The dictionary is put into the HashMap from file with the @link readDictionary method 
    */
    public void breakVigenere (String encrypted) {
        HashMap<String,HashSet<String>> dictionaries = new HashMap<>();
        // Prompts to select language dictionaries
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            String filename = f.getName();
            if(!dictionaries.containsKey(filename)){
                dictionaries.put(filename, readDictionary(fr));
            }
        }
        String decrypted = breakForAllLangs(encrypted, dictionaries);
        System.out.println(decrypted);
    }

    /*
    * Returns a HashSet of Strings by reading @param FileResource and adding its each line as an element
    */
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> words = new HashSet<>();
        for(String word : fr.lines()){
            words.add(word.toLowerCase());
        }
        return words;
    }
    
    /*
    * Returns the number of valid(real) words present in the @param message String by
    * splitting it to words first, then searches that word in the @param dictionary HashSet
    * and counts if it's present.
    */
    public int countWords(String message, HashSet<String> dictionary){
        int validWords = 0;
        String[] words = message.toLowerCase().split("\\W+");
        for(String word : words){
            if(dictionary.contains(word)) validWords++;
        }
        return validWords;
    }
    
    /*
    * Returns a String which contains: 1. Key, 2. Keylength with which the decryption was done,
    * 3. Total real words present in the decrypted text and the 4. Decrypted text itself.
    * 
    * The @param enrypted String is decrypted with the help of the @param dictionary by
    * finding its most common character with @link mostCommonCharIn() method.
    * It creates an array of keys with @link tryKeyLength() method then it is used for the
    * @link VigenereCipher object constuctor. That object is used for decryption by
    * its @link decrypt() method.
    * The decrypted String is checked for valid(real) words by @link countWords() method,
    * and if it contains the most valid(real) words then this decryption is returned.
    */
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int[] keys = null;
        int realWords = 0;
        String wellDecrypted = "";
        String key = "";
        
        for(int i=1; i<100; i++){ // could be encrypted.length()
            int[] currKeys = tryKeyLength(encrypted, i, mostCommonCharIn(dictionary));
            VigenereCipher vc = new VigenereCipher(currKeys);
            String tryDecrypted = vc.decrypt(encrypted);
            
            if(realWords < countWords(tryDecrypted, dictionary)){
                realWords = countWords(tryDecrypted, dictionary);
                wellDecrypted = tryDecrypted;
                keys = currKeys;
                key = convertArrToString(keys);
            }
        }
        return "Key is: " + key + "\nKeylength is: " + keys.length + "\nTotal valid words in decrypted: " + realWords + "\nDecryption is:\n" + wellDecrypted;
    }
    
    /*
    * Returns the most common (most frequent) character from @param dictionary HashSet
    */
    public char mostCommonCharIn(HashSet<String> dictionary){
        HashMap<Character, Integer> counter = new HashMap<>();
        for(String word : dictionary){
            char[]wordsChars = word.toLowerCase().toCharArray();
            for(int i=0;i<wordsChars.length;i++){
                if(!counter.containsKey(wordsChars[i])){
                    counter.put(wordsChars[i], 1);
                }
                else{
                    counter.put(wordsChars[i], counter.get(wordsChars[i])+1);
                }
            }    
        }
        int largest = 0;
        char mostCommon = '\0';
        for(Character c : counter.keySet()){
            if(counter.get(c) > largest){
                mostCommon = c;
                largest = counter.get(c);
            }
        }
        return mostCommon;
    }
    
    /*
    * It starts the encryption process of the @param encrypted String by iterating thorugh all languages
    * in the @param dictionary and calling the @link breakForLanguage() method for that particular language.
    * After that decryption is done it checks how many valid(real) words are in the decrypted String by
    * calling the @link countWords() method.
    * At the end it keeps that decryption which is made by the language where the most valid(rel)
    * words are in.
    * Returns the decryption and the language with which it was decrypted.
    */
    public String breakForAllLangs(String encrypted, HashMap<String,HashSet<String>> languages){
        String chosenLanguage = "";
        String bestDecryption = "";
        int largest = 0;
        for(String language : languages.keySet()){
            String decryptedForALanguage = breakForLanguage(encrypted, languages.get(language));
            // See the number of valid words in this decryption
            int numOfValidWords = countWords(decryptedForALanguage, languages.get(language));
            if(numOfValidWords > largest){
                bestDecryption = decryptedForALanguage;
                chosenLanguage = language;
                largest = numOfValidWords;
            }
        }
        return "Chosen language is: " + chosenLanguage + "\n" + bestDecryption;
    }
    
    /*
    * Helper method to convert an array of ints into a formatted String
    */
    private static String convertArrToString(int[] arr){
        String converted = "";
        for(int i=0;i<arr.length;i++){
            converted += arr[i] + ", ";
        }
        return converted.substring(0, converted.length()-2);
    }
    
}
