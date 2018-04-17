package model;

public enum Suit{
  
  Clubs,
  Spades,
  Hearts,
  Diamonds;
  
  public static Suit getSuit(String suit){
      switch(suit){
          case "C" : return Clubs;
          case "S" : return Spades;
          case "H" : return Hearts;
          case "D" : return Diamonds;
      }
      return null;
  }
}
