import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    // Recursively goes through word until restOfWord is empty
    public void makeWords (String beginning, String restOfWord) {
        if (restOfWord.length() == 0) {
            words.add(beginning);
            return;
        }
        for (int i = 0; i < restOfWord.length(); i++) {
            words.add(beginning + restOfWord.charAt(i));
            //substring 0,i and i+1 so that you do not double use i
          makeWords(beginning + restOfWord.charAt(i),
                  restOfWord.substring(0, i) + restOfWord.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergeSort(words, 0, words.size() - 1);
    }

    public ArrayList<String> mergeSort (ArrayList<String> arr, int left, int right) {

        if (left == right) {
            ArrayList<String> newArr = new ArrayList<>();
            newArr.add(arr.get(left));
            return newArr;
        }

        int med = (right + left) / 2;
        ArrayList<String> arrOne = mergeSort(arr, left, med);
        ArrayList<String> arrTwo = mergeSort(arr, med + 1, right);

        return merge(arrOne, arrTwo);
    }

    public ArrayList<String> merge(ArrayList<String> arrOne, ArrayList<String> arrTwo) {
        ArrayList<String> sol = new ArrayList<>(arrOne.size() + arrTwo.size());
        int i = 0, j = 0;

        while (i < arrOne.size() && j < arrTwo.size()) {
            if (arrOne.get(i).compareTo(arrTwo.get(j)) <= 0) {
                sol.add(arrOne.get(i++));
            }
            else
            {
                sol.add(arrTwo.get(j++));
            }
        }
        while (i < arrOne.size()) {
            sol.add(arrOne.get(i++));
        }

        while (j < arrTwo.size()) {
            sol.add(arrTwo.get(j++));
        }

        return sol;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            if (!found(words.get(i),0, DICTIONARY_SIZE)) {
                words.remove(i);
                // So you don't skip next word
                i--;
            }
        }
    }

    public boolean found(String word, int left, int right) {
        int mid = (left + right) / 2;
        if (word.equals(DICTIONARY[mid])) {
            return true;
        }

        if (left == right) {
            return false;
        }

        if (word.compareTo(DICTIONARY[mid]) < 0) {
            return found(word, left, mid);
        }

        return found(word, mid + 1, right);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
