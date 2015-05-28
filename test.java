package com.lugtech;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PoemGenerator {
    
  private static final List<Sentence> sentences = new ArrayList<Sentence>();
  
  public static void main(String[] args) throws FileNotFoundException {
    Scanner scanner = new Scanner(PoemGenerator.class.getClassLoader().getResourceAsStream("/com/lugtech/henrydavidthoreau_walden.txt"));
    scanner.useDelimiter("\\.");
    while (scanner.hasNext())
      sentences.add(new Sentence(scanner.next()));
    
    int count = 0;
    int lines = 10 + (int) (Math.random() * 6);
    while (count < lines) {
      int i = 100 + (int) (Math.random() * (sentences.size() - 500));
      sentences.get(i).magic();
      if (sentences.get(i).words.size() < 1)
        continue;
      System.out.println(" " + sentences.get(i));
      if (Math.random() < 0.24)
        System.out.println();
      count++;
    }
    
    scanner.close();
  }
  
  public static class Sentence {
    
    private final List<Word> words = new ArrayList<Word>();
    
    public Sentence(String s) {
      s = s.replaceAll("[\\r\\n]+", " ");
      s = s.replaceAll("\"", "");
      s = s.replaceAll("_", "");
      s = s.toLowerCase();
      String[] words = s.split("\\s+");
      for (String wordStr : words) {
        Word word = new Word(wordStr);
        if (word.word.length() < 1)
          continue;
        this.words.add(word);
      }
    }
    
    @Override
    public String toString() {
      String[] wordsStr = new String[words.size()];
      for (int i = 0; i < words.size(); i++)
        wordsStr[i] = words.get(i).toString();
      
      return String.join(" ", wordsStr);
    }
    
    public void magic() {
      
      List<Word> removed = new ArrayList<Word>();
      for (int i = 0; i < words.size(); i++) {
        String word = words.get(i).word;
        if (word.length() == 1) {
          if (Math.random() < 0.5)
            removed.add(words.get(i));
          else if (Math.random() < 0.15)
            words.get(i).word += "n";
        }
        else if (word.length() / 10f < Math.random() + Math.random())
          removed.add(words.get(i));
      }
      words.removeAll(removed);
      int length = (int) (5 + Math.random() * 3);
      
      // SHUFFLING
      if (Math.random() < 0.25 || words.size() < 3)
        Collections.shuffle(words);
      else {
        int split = 2 + (int) (Math.random() * (words.size() - 2));
        List<Word> a = new ArrayList<Word>(words.subList(0, split));
        if (Math.random() < 0.3)
          Collections.shuffle(a);
        List<Word> b = new ArrayList<Word>(words.subList(split, words.size()));
        if (Math.random() < 0.3)
          Collections.shuffle(b);
        
        words.clear();
        if (Math.random() < 0.7) {
          words.addAll(a);
          words.addAll(b);
        }
        else {
          words.addAll(b);
          words.addAll(a);
        }
      }
      
      for (int i = 1; i < words.size(); i++) {
        String word = words.get(i).word;
        if (word.length() < 2)
          continue;
        if (word.length() / 20f < Math.random() * Math.random()) {
          words.get(i).word = Character.toUpperCase(word.charAt(0)) + word.substring(1);
          i += 5 * Math.random();
        }
      }
      
      for (int i = 1; i < words.size(); i++) {
        String word = words.get(i).word;
        if (word.length() / 10f > Math.random() + Math.random() + Math.random()) {
          int s = 1 + (int) (Math.random() * (word.length() - 1));
          words.get(i).word = word.substring(0, s);
          words.get(i).punctuation = "\n";
          words.add(i + 1, new Word(word.substring(s)));
          
          length += (int) (5 + Math.random() * 3);
        }
      }
      
      if (length < words.size()) {
        List<Word> cropped = words.subList(length, words.size());
        words.removeAll(cropped);
        Word last = words.get(words.size() - 1);
        if (last.punctuation != null)
          last.punctuation = null;
      }
    }
  }
  
  public static class Word {
    
    public String word;
    public String punctuation;
    
    public Word(String raw) {
      raw = raw.trim();
      String[] val = raw.split("\\b", 2);
      word = val[0];
      if (val.length > 1)
        punctuation = val[1];
      else
        punctuation = null;
    }
    
    @Override
    public String toString() {
      return word + (punctuation != null ? punctuation : "");
    }
  }
}
