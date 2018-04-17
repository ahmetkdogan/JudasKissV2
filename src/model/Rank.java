package model;


public enum Rank {
  
  Ace,
  Two,
  Three,
  Four,
  Five,
  Six,
  Seven,
  Eight,
  Nine,
  Ten,
  Joker,
  Queen,
  King;
  
  public static Rank getRank(String rank){
      switch(rank){
          case "A" : return Ace;
          case "2" : return Two;
          case "3" : return Three;
          case "4" : return Four;
          case "5" : return Five;
          case "6" : return Six;
          case "7" : return Seven;
          case "8" : return Eight;
          case "9" : return Nine;
          case "10" : return Ten;
          case "J" : return Joker;
          case "Q" : return Queen;
          case "K" : return King;
      }
      return null;
  }
   
}
