/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String, ArrayList<String>>  lettersToWord = new  HashMap<String, ArrayList<String>> ();
    HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
//As you process the input words, call sortLetters on each of them then check whether lettersToWord already contains an entry for that key.
// If it does, add the current word to ArrayList at that key. Otherwise, create a new ArrayList, add the word to it and store in the HashMap with the corresponding key.
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }else{
                ArrayList<String> ana = new ArrayList<String>();
                sizeToWords.put(word.length(),ana);
                sizeToWords.get(word.length()).add(word);
            }

            if(lettersToWord.containsKey(sortLetters(word))){
                lettersToWord.get(sortLetters(word)).add(word);
            }else{
                ArrayList<String> anagrams = new ArrayList<String>();
                anagrams.add(word);
                lettersToWord.put(sortLetters(word),anagrams);
            }
            wordList.add(word);
            wordSet.add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)) {
            if (base.toLowerCase().contains(word.toLowerCase())) {
                return false;
            }
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for (String object: wordList) {
            if(targetWord.length() == object.length()){
                if(sortLetters(targetWord).equals(sortLetters(object))){
                    result.add(object);
                }

            }
        }
        return result;
    }

    public String sortLetters(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        String newWord = new String(chars);
        return newWord;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(char c: alphabet){
            if(lettersToWord.containsKey(sortLetters(word + c))){
                for (String anagram: lettersToWord.get(sortLetters(word + c))) {
                    result.add(anagram);
                }

            }

        }

        return result;

    }

    public String pickGoodStarterWord() {

        int indexOfWord = random.nextInt(wordList.size());

        if((getAnagramsWithOneMoreLetter(sortLetters(wordList.get(indexOfWord))).size() >= 5) && (wordLength == (wordList.get(indexOfWord).length()))){

            if(wordLength < MAX_WORD_LENGTH){
               wordLength = wordLength + 1;
            }
            return wordList.get(indexOfWord);
        }
        else return pickGoodStarterWord();
    }
}
