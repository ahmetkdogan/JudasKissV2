package model;


public class Card {
    private Suit suit;
    private Rank rank;
    private String id;
    
    public Card(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
        this.id = buildId();
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString(){
        return "Rank: "+ getRank() + " Suit: " + getSuit();
    }
    
    protected String buildId() {

    String suitChar = null;
    String rankChar = null;

    Rank rank = (Rank)this.getRank();

    switch (rank) {
      case Ace:
        rankChar = "A";
        break;
      case Two:
        rankChar = "2";
        break;
      case Three:
        rankChar = "3";
        break;
      case Four:
        rankChar = "4";
        break;
      case Five:
        rankChar = "5";
        break;
      case Six:
        rankChar = "6";
        break;
      case Seven:
        rankChar = "7";
        break;
      case Eight:
        rankChar = "8";
        break;
      case Nine:
        rankChar = "9";
        break;
      case Ten:
        rankChar = "10";
        break;
      case Joker:
        rankChar = "J";
        break;
      case Queen:
        rankChar = "Q";
        break;
      case King:
        rankChar = "K";
        break;
    }

    Suit suit = (Suit) this.getSuit();

    switch (suit) {
      case Clubs:
        suitChar = "C";
        break;
      case Diamonds:
        suitChar = "D";
        break;
      case Hearts:
        suitChar = "H";
        break;
      case Spades:
        suitChar = "S";
        break;
    }

    return rankChar + suitChar+"";
  }
    
}
