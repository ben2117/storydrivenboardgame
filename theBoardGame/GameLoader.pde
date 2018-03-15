public class GameLoader{
  XML gameData;

  public GameLoader(){
     gameData = loadXML("gameData.xml");
  }
  
  public ArrayList<Tile> getTiles(){
    //these are all of the game tiles
    ArrayList<Tile> tiles = new ArrayList<Tile>();
    //we get data from xml
    XML tileContainerData = gameData.getChild("tileContainer");
    XML[] tileData = tileContainerData.getChildren("tile");

    //we loop through the tiles
    for(int i = 0; i < tileData.length; i++){
      ArrayList<Card> cards = new ArrayList<Card>();
        try{
          //each tiles has three tiers of cards
          ArrayList<Card> firstTier = getTierOfCards(tileData[i].getChild("firstTier"));
          //ArrayList<TileCard> secondTier = getTierOfCards(tileData[i].getChild("secondTier"));

          //here we add all of the tiers together to form the deck
          cards = firstTier; //this will be a concatination function
         
        } catch (Exception e){};
        //once we have all the tiers we can shuffle them ..
        //
        //TODO: SHUFFLE CARDS
        //
        //and then add them to the tile
        tiles.add(
          new Tile(
            tileData[i].getString("id"),
            tileData[i].getString("name"), 
            tileData[i].getString("description"),
            cards
          )
        );
        //printAllTheShit(tiles.get(tiles.size()-1));
    }
    return tiles;
  }

  private ArrayList<Card> getTierOfCards(XML xmlCardTier){

    ArrayList<Card> cards = new ArrayList<Card>();
    XML[] xmlCardsInTier = xmlCardTier.getChildren("card");

    for(int i = 0; i < xmlCardsInTier.length; i++){
      //the items are a feature of the tileCard
      if(xmlCardsInTier[i].getString("type").equals("ItemCard")){
        ArrayList<Item> cardItems = getCardItems(xmlCardsInTier[i].getChildren("item"));
        cards.add(
          new ItemCard(
            xmlCardsInTier[i].getString("id"),
            xmlCardsInTier[i].getString("order"),
            xmlCardsInTier[i].getString("text"),
            cardItems
          )
        );
      }
      else if (xmlCardsInTier[i].getString("type").equals("MonsterCard")) {
        cards.add(
          new MonsterCard(
            xmlCardsInTier[i].getString("id"),
            xmlCardsInTier[i].getString("order"),
            xmlCardsInTier[i].getString("text"),
            xmlCardsInTier[i].getInt("health"),
            xmlCardsInTier[i].getInt("attack"),
            xmlCardsInTier[i].getInt("defense"),
            xmlCardsInTier[i].getInt("speed")
            )
        );
      }
    }
    return cards;
  }

  private ArrayList<Item> getCardItems(XML[] xmlCardItems){
    ArrayList<Item> cardItems = new ArrayList<Item>();
    
    for(int i = 0; i < xmlCardItems.length; i++){
      cardItems.add(
         new Item (
           xmlCardItems[i].getString("description"),
           xmlCardItems[i].getString("text"),
           xmlCardItems[i].getInt("attack"),
           xmlCardItems[i].getInt("speed"),
           xmlCardItems[i].getInt("defense"),
           xmlCardItems[i].getInt("heal"),
           xmlCardItems[i].getInt("uses")
         )
       );
    }
    return cardItems;
  }

  /*private void printAllTheShit(Tile tile){
    println("Tile " + tile.getTileName());
    println("   Tile Cards");
    ArrayList<Card> cards= tile.getCards();
    for(Card tc : cards){
      println("   Card " + tc.getId());
      println("   Text " + tc.getText());
      println("      Card Items");
      for(CardItem ci : tc.getCardItems()){
        println("      description " + ci.getDescription());
  
      }
  
    }
  }
  */
  
}