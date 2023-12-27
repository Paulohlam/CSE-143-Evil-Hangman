// Paul Lam
// Section AG, Neel jog
// A4 AssassinManager
// HangmanManager keeps track of the state of the game of hangman and updates
// as the game progresses.

import java.io.*;
import java.util.*;

public class HangmanManager {
   // The current guessing pattern.
   private String pattern;
   // Keeps track of the remaining amount of guesses.
   private int remainingGuesses;
   // The set of possible guessing words.
   private Set <String> setOfWords;
   // The set of guessed letters made by the user.
   private Set <Character> setOfGuesses;

   // Purpose: Creats the dictionary of words used to guess in
   // the game of hangman. Initializes the max number of guesses, length of the word
   // and words used in the game.
   // Pre: The length must be greater than 1. The max must be greater than 0.
   // If the condition is violated will throw IllegalArgumentException.
   // Post: Initializes the details of the game of hangman (word length, max number of
   // guesses and the set of words to guess from.
   // Parameters: dictionary = list of potential words to guess from.
   // Length = length of guessing word.
   // max = the max ammount of guesses.
   public HangmanManager(Collection <String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      remainingGuesses = max;
      setOfWords = new TreeSet <String> ();
      setOfGuesses = new TreeSet <Character> ();
      pattern = "-";
      for (int i = 1; i < length; i++) {
         pattern += " -";
      }
      for (String s: dictionary) {
         if (s.length() == length) {
            setOfWords.add(s);
         }
      }
   }

   // Purpose: Returns the current set of guessing words.
   // Pre: Set of words must be greater than 0. 
   // The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0.
   // Post: Returns the current set of guessing words.
   public Set <String> words() {
      return setOfWords;
   }

   // Purpose: Returns the current amount of guesses left. 
   // Pre: The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0. 
   // Post: Returns the current amount of guesses remaining.
   public int guessesLeft() {
      return remainingGuesses;
   }

   // Purpose: Returns the already guessed letters by the user.
   // Pre: The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0.
   // Post: Returns all of the current guessed letters by the user.
   public Set <Character> guesses() {
      return setOfGuesses;
   }
   
   // Purpose: Takes into account the previous guesses by the user and 
   // returns the current pattern of the possible guessing word.
   // Pre: Set of words must not be empty.
   // If the conditions are vioalted, throws an IllegalStateException.
   // Post: Returns the current pattern of the possible guessing word taking
   // into account the previus guesses made by the user.
   public String pattern() {
      if (setOfWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return pattern;
   }

   // Purpose: Takes into account the next guess made by the user and 
   // decides the current list of the possible guessing words. Updates the 
   // number of guesses left and returns the number of times the guessed letter
   // occurs in the new pattern.
   // Pre: The set of words must not be empty or the remaining guesses must be
   // greater than 1.
   // If conditions are violated, throws an IllegalStateException.
   // If the IllegalStateException is not thrown and a correct letter is guessed,
   // throws an IllegalArgumentException.
   // Post: Takes the next guess made by the user and decides the list of possible 
   // guessing words. Updates the count of guesses left and returns the occurence of the
   // next guess in the new pattern.
   // Parameter: Guess = the guess made by the user.  
   public int record(char guess) {
      if (setOfWords.isEmpty() || remainingGuesses < 1) {
         throw new IllegalStateException();
      }
      if (!setOfWords.isEmpty() && remainingGuesses > 1 && setOfGuesses.contains(guess)) {
         throw new IllegalArgumentException();
      }
      int initialSize = setOfWords.size();
      setOfGuesses.add(guess);
      Map <String, Set <String>> map = patternMaker();
      findBestPattern(map);
      int occurrenceTracker = occurrence(guess);
      return occurrenceTracker;
   }

   // Purpose: Returns the patterns of the possible guessing word.
   // Pre: The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0.
   // Post: Returns the patterns of the possible guessing word.
   private Map<String, Set<String>> patternMaker() {
      Map <String, Set <String>> patternMap = new TreeMap<>();
      for (String s: setOfWords) {
         String results = "";
         for (int i = 0; i < ((pattern.length() - 1) / 2); i++) {
            if (setOfGuesses.contains(s.charAt(i))) {
               results = results + s.charAt(i) + " ";
            } else {
               results += "- ";
            }
         }
         if (setOfGuesses.contains(s.charAt((pattern.length() - 1) / 2))) {
            results = results + s.charAt((pattern.length() - 1) / 2);
         } else {
            results += "-";
         }
         if (!patternMap.containsKey(results)) {
            patternMap.put(results, new TreeSet<>());
         }
         patternMap.get(results).add(s);
      }
      return patternMap;
   }

   // Purpose: Finds the best pattern.
   // Pre: The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0.
   // Post: Finds the best pattern of the possible guessing word.
   private void findBestPattern(Map<String, Set<String>> map) {
      String biggestKey = "";
      int keySize = 0;
      for (String s: map.keySet()) {
         if (map.get(s).size() > keySize) {
            biggestKey = s;
            keySize = map.get(s).size();
         }
      }
      pattern = biggestKey;
      setOfWords = map.get(pattern);
   }
   
   // Purpose: returns how many times the guessed letter occurs in the new pattern.
   // Pre: The length of the word must be greater than 1.
   // The maxium ammount of guesses must be greater than 0.
   // The set of possible guessing words must not be empty. 
   // Post: Returns the ammount of times the guessed letter occurs in the new pattern.
   // Parameter: Guess = the letter guessed by the user. 
   private int occurrence(char guess) {
      int occurrence = 0;
      for (char ch: pattern.toCharArray()) {
         if (ch == guess) {
            occurrence++;
         }
      }
      if (occurrence == 0) {
         remainingGuesses--;
      }
      return occurrence;
   }
}