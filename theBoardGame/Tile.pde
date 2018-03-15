class Tile{
  private String name;
  private String description;
  private String id;
  //private Player player;
  private ArrayList<Card> cards;
  private int cardDrawPlace;

  private MonsterPosition[] monsterPositions;
  private int timeSpentOnTile;


  private ArrayList<Button> buttons; 
  //BUTTON TO DRAW NEXT TILE CARD
  //BUTTON TO ENGAGE CLOSEST MONSTER
  
  public Tile(String id, String name, String description, ArrayList<Card> cards){
     this.id = id;
     this.name = name;
     this.description = description;
     //this.player = null;
     this.cards = cards;
     this.cardDrawPlace = 0;

     this.monsterPositions = new MonsterPosition[3];
     monsterPositions[0] = new MonsterPosition(0, 400, 150, 100);
     monsterPositions[1] = new MonsterPosition(1, 100, 250, 200);
     monsterPositions[2] = new MonsterPosition(2, 400, 350, 300);


     this.timeSpentOnTile = 0;
  }

  public void checkForMonsterAttack(){
    if(monsterPositions[2].isMonsterThere()){
      game.enterCombat(2);
    }
  }

  public MonsterCard getMonsterAt(int id){
    return monsterPositions[id].getMonsterCard();
  }

  public boolean positionsAreEmpty(){
    boolean result = false;
    for(int i = 0; i < monsterPositions.length; i++){
      if(monsterPositions[i].isMonsterThere()){
        result = true;
      }
    }
    return result;
  }

  public void isMonsterDead(){
    for(int i = 0; i < monsterPositions.length; i++){
      monsterPositions[i].checkForDead();
    }
  }

  public Card getNextCard(){
    try{
      Card drawnCard = cards.get(cardDrawPlace);
      cardDrawPlace++;
      return drawnCard;
    }catch(IndexOutOfBoundsException e){
      println("there was a problem drawing the card, everything is probably fucked now");
      return null;
    }
  }

  //TODO: after certain time passes player is able to move from tile
  
  public void drawTile(){
    fill(200);

    //monster position holders
    //rect(400, 150, 125, 25);
    //rect(100, 250, 250, 40);
    //rect(400, 350, 350, 50);
    monsterPositions[0].drawMonsters();
    monsterPositions[1].drawMonsters();
    monsterPositions[2].drawMonsters();

    fill(50);
    text(this.name, 300, 100);
  }

  public void detectClick(){
    for(int i = 0; i < monsterPositions.length; i++){
      monsterPositions[i].detectClick();
    }
  }
  
  public String getTileName(){
    return name;
  }

  public ArrayList<Card> getCards(){
    return cards;
  }

  public void monstersMove(){
    if(!monsterPositions[2].isMonsterThere()){
      monsterPositions[2].setMonsterCard(monsterPositions[1].moveMonster());
    }
    if(!monsterPositions[1].isMonsterThere()){
      monsterPositions[1].setMonsterCard(monsterPositions[0].moveMonster());
    }
  }

  public void handleMonsterCard(Card currentCard){
    if(currentCard instanceof MonsterCard){
      MonsterCard currentMonsterCard = (MonsterCard) currentCard;
      for(int i = 0; i < monsterPositions.length; i++){
        if(!monsterPositions[i].isMonsterThere()){
          monsterPositions[i].setMonsterCard(currentMonsterCard);
          break;
        }
      }
    }
  }

  
}