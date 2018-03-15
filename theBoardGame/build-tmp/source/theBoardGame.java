import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Collections; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class theBoardGame extends PApplet {

public enum GameState { MENU, SETTING, GAME }

GameState gameState = GameState.MENU;

ActionHandler actionHandler;
Game game;
Menu menu;
GameLoader gameLoader;
ImageLoader imageLoader;
boolean loading;

ArrayList<Callback> callbacks = new ArrayList<Callback>();

public void addCallback(String callFunction, int timer, Button button){
  callbacks.add(new Callback(callFunction, timer, button));

}

public void settings() {
  //fullScreen(JAVA2D);
  //this fixes crash when not in landscape mode.
  orientation(LANDSCAPE);
  size(800, 600, JAVA2D);
}

public void setup() {
   imageLoader = new ImageLoader();
   gameLoader = new GameLoader();
   menu = new Menu();
   actionHandler = new ActionHandler();
   imageMode(CENTER);
}

public void draw() {
  //background(250);
  for(Callback c : callbacks){
    c.incTime();
    if(c.toDestroy()){
      callbacks.remove(c);
      break;
    }
  }
  switch(gameState){
   case MENU: menu.drawMenu(); break;
   case GAME: game.drawGame(); break;
   case SETTING: println("setting"); break;
  }
}

public void mousePressed() {
  switch(gameState){
     case MENU: menu.detectClick(); break;
     case GAME: game.detectClick(); break;
     case SETTING: println("setting"); break;
  }
}
public class ActionHandler{
  
  public void startGame(){
    game = new Game();
    gameState = GameState.GAME;
    menu = null;
  }
  
  public void camp(){
    gameState = GameState.MENU;
    menu = new Menu();
  }
  
  public void explore(){
  	println("Explore was done diddly done");
  }
  
  //the player moves around on the board, therefore the board controlls
  //the movement of the player
  
  public void north(){
    game.board.movePlayer('n');
  }
  
  public void south(){
    game.board.movePlayer('s');
  }
  public void east(){
    game.board.movePlayer('e');
  }
  public void west(){
    game.board.movePlayer('w');
  }

  public void nextTurn(){
    //if(game.board.allowNextTurn()){
      if(game.board.nextTurn())
        addCallback("nextTurnCallback", 50, null);
      else 
        nextTurnCallback();
    //}
  }

  public void nextTurnCallback(){
    game.board.nextTurnCallback();
  }

  public void skipCinimatic(){
    game.cinimatic.skip();
  }

  public void clickCardItem(int id){
    game.board.clickCardItem(id);
  }

  public void playerScreen(){
    game.board.togglePlayerScreen();
  }

  public void monsterOk(){
    game.board.handleMonsterCard();
  }

  public void attackMonster(int id){
    game.enterCombat(id);
  }

  public void moveButton(){
    game.board.player.toggleMovementOptions();
  }

  public void useItem(int id){
    try{
      game.combat.useItem(id);
    }catch(Exception e){
      println("useItem was called, likely out of sink");
    }
  }
}


class Board{
  int rows;
  int columns;
  Player player;
  PlayerPosition playerPosition;

  boolean showPlayer;
  
  ArrayList<Button> boardButtons;

  Tile[][] boardTiles;

  Card currentCard;
  
  public Board(){
    rows = 3;
    columns = 3;
    boardTiles = new Tile[rows][columns];
    player = new Player();
    currentCard = null;
    showPlayer = false;
    //set up the game
    layOutTheBoard();
    boardButtons = new ArrayList<Button>();
    boardButtons.add(new Button("Next Turn", ButtonAction.NEXT_TURN, 550, 550, 130, 32));
    boardButtons.add(new Button("Inventory", ButtonAction.PLAYER_SCREEN, 200, 550, 130, 32));
    //boardButtons.add(new Button("Combat", ButtonAction.COMBAT, 350, 550));
    

  }

  public void togglePlayerScreen(){
    showPlayer = !showPlayer;
  }
 
  private void layOutTheBoard(){
    ArrayList<Tile> tiles = gameLoader.getTiles();
    Collections.shuffle(tiles);
    int tilePosition = 0;
    try{
      for(int i = 0; i<rows; i++)
        for(int j = 0; j<columns; j++){
            boardTiles[i][j] = tiles.get(tilePosition);
            tilePosition++;
            if(boardTiles[i][j].getTileName().equals("City")){ 
              player.setPlayersTile(boardTiles[i][j]);//board[i][j].movePlayerToTile(player);
              playerPosition = new PlayerPosition(i, j, rows, columns);
            }
        }
    } catch (IndexOutOfBoundsException e) {
      println("Problem initilizing board, check game data has enough tiles");
    }
    println(playerPosition.getIPos());
    println(playerPosition.getJPos());
  }

  public boolean nextTurn(){
    if(currentCard==null){
      player.playersTile.monstersMove();
    }
    return player.playersTile.positionsAreEmpty();
  }

  public void nextTurnCallback(){
    if(currentCard==null){
      currentCard = player.getPlayersTile().getNextCard();
      player.incrementEnergy();
      player.playersTile.checkForMonsterAttack();
    }
  }

  public void isMonsterDead(){
    player.getPlayersTile().isMonsterDead();
  }

  //nextTurn
    //monster attacks if able
    //monsters move animation()
    //increment energy

  //end turn callback from animation
    //currentCard = player.getPlayersTile().getNextCard();

  public void clickCardItem(int id){
    try{
      player.addCardItem(currentCard.getItem(id));
      currentCard = null;
    } catch (Exception e){}
  }


  public void handleMonsterCard(){
    player.playersTile.handleMonsterCard(currentCard);
    currentCard = null;
  }
  
  public void drawBoard(){
    player.getPlayersTile().drawTile();  

    for(Button b : boardButtons){
      b.drawButton();
    }
    if(currentCard!=null){
      currentCard.drawCard();
    }

    //draw player screen
    player.drawPlayer();
    if(showPlayer){
      player.drawPlayerItems();
    }


  }
  
  public void detectClick(){
    for(Button b : boardButtons){
     b.detectClick();
    }
    player.detectClick();
    if(currentCard!=null){
      currentCard.detectClick();
    }
  }

  //TODO: draw the buttons

  
  public void movePlayer(char direction){
    boolean successfulMove;
    switch(direction){
      case 'n': successfulMove = playerPosition.moveNorth(); break;
      case 's': successfulMove = playerPosition.moveSouth(); break;
      case 'e': successfulMove = playerPosition.moveEast(); break;
      case 'w': successfulMove = playerPosition.moveWest(); break;
      default: successfulMove = false;
    }
    println(playerPosition.getIPos());
    println(playerPosition.getJPos());
    
    if(successfulMove){
      player.toggleMovementOptions();
      player.useMovementEnergy();
      player.setPlayersTile(boardTiles[playerPosition.getIPos()][playerPosition.getJPos()]);
    }
    
  }
 
}

//the board has 3 monster positions
public enum ButtonAction { START_GAME, NEXT_TURN, PLAYER_SCREEN, CARD_ITEM, 
  MONSTER_OK, MONSTER_VIEW, ATTACK_MONSTER, MONSTER_DEFEND, 
  SKIP, COMBAT, USE_ITEM, MOVE, NORTH, EAST, SOUTH, WEST, CAMP, EXPLORE  }

public class Button{
  float x, y;
  ButtonAction action;
  String text;
  int sizeWidth, sizeHeight;

  boolean clicked;
  int id;

  boolean greyOut;

  PImage image;;

  public Button(String text, ButtonAction action, float x, float y, int sizeWidth, int sizeHeight, int id){
    this(text, action, x, y, sizeWidth, sizeHeight);
    this.id = id;
  }

  public Button(String text, ButtonAction action, float x, float y, int sizeWidth, int sizeHeight){
    this.text = text;
    this.action = action;
    this.x = x;
    this.y = y;
    this.sizeWidth = sizeWidth;//130; //130, 32
    this.sizeHeight = sizeHeight;//32;
    clicked = false;
    image = null;
    for(ButtonImageContainer bic : imageLoader.getImages()){
      if(bic.getButtonText().equals(text)){
        image = bic.getPng();
      }
    }
  }

  public void greyOut(){
    greyOut = true;
  }

  public void drawButton(){

    if(clicked){
      fill(255, 0, 0);
    } else if (greyOut){
      fill(200);
    }else 
      fill(50);
    //rect(x-50, y-50, 55, 55);
    

    if(image!=null){
      image(image, x-32, y-32);
    } else {
      textSize(32);
      text(text, x, y);
    }
  }

  public void detectClick(){
    if(mouseX >= x && mouseX <= x+sizeWidth && mouseY >= y-sizeHeight && mouseY <= y){
      addCallback("buttonClick", 25, this);
      clicked = true;
    }
  }
  
  public void actionClick(){
      println(action);
      clicked=false;
      switch(action){
        case START_GAME: actionHandler.startGame(); break;
        case CAMP: actionHandler.camp(); break;
        case EXPLORE: actionHandler.explore(); break;
        case NORTH: actionHandler.north(); break;
        case SOUTH: actionHandler.south(); break;
        case EAST: actionHandler.east(); break;
        case WEST: actionHandler.west(); break;
        case SKIP: actionHandler.skipCinimatic(); break;
        case NEXT_TURN: actionHandler.nextTurn(); break;
        case CARD_ITEM: actionHandler.clickCardItem(id); break;
        case PLAYER_SCREEN: actionHandler.playerScreen(); break;
        case MONSTER_OK: actionHandler.monsterOk(); break;
        case ATTACK_MONSTER: actionHandler.attackMonster(id); break;
        case MOVE: actionHandler.moveButton(); break;
        case USE_ITEM: actionHandler.useItem(id); break;
      }
  }

}
class Callback{
  Game gamePointer;
  String callFunction;
  int timer;
  int time;
  Button button;
  boolean destroy;

  Callback(String callFunction, int timer, Button button){
    this.destroy = false;
    this.time = 0;
    this.gamePointer = game;
    this.callFunction = callFunction;
    this.timer = timer;
    this.button = button;
    println("callback added" + this.timer);
  }

  public boolean toDestroy(){
    return destroy;
  }

  public void incTime(){
    //println(time + " and " + timer);
    if(destroy) return;
    time++;
    if(time>timer){
      destroy = true;
      callback();
    }
  }

  private void callback(){
    println("Callback");
    if(callFunction.equals("buttonClick")){
      button.actionClick();
    }
    if(callFunction.equals("nextTurnCallback")){
      actionHandler.nextTurnCallback();
    }
  }
  
}
class Card{

  private String id;
  private String order;
  private ArrayList<String> styledText;
  private String text;

  public Card(){}

  public Card(String id, String order, String text){
  	this.id = id;
    this.order = order;
    try{
    	this.styledText = operateOnText(text);
    } catch(Exception e){
    	println("problem operating on text");
    }
    this.text = text;
  }

  public Item getItem(int id) {
    return null;
  }

  public void drawCard(){
  	fill(70);
  	rect(300, 100, 400, 400);
  	fill(200);
  	textSize(24);
  	int count = 30;
  	for(String t: styledText){
  		text(t, 350, 150+count);
  		count+=30;
  	}
  }

   public String getId(){
  	return id;
  }
  public String getText(){
	return text;
  }

  public void detectClick(){}

   private ArrayList<String> operateOnText(String text){
  	ArrayList<String> styledText = new ArrayList<String>();
  	String[] opT = split(text, ' ');
  	int lc = 0;
  	while(lc < opT.length){
  		String opO = "";
  		for(int i = 0; i < 4; i++){
  			opO = opO + opT[lc] + " ";
  			lc++;
  			if(lc == opT.length) break;
  		}
  		println(opO);
  		styledText.add(opO);
  	}
  	return styledText;
  }
}
enum CinimaticState {INTRO_CINIMATIC}
class Cinimatic{
  private ArrayList<Button> buttons;
  public CinimaticState cinimaticState;
  private int frameCount;

  public Cinimatic(CinimaticState cinimaticState){
    buttons = new ArrayList<Button>();
    buttons.add(new Button("Skip", ButtonAction.SKIP, 50, 100, 130, 32));
    this.cinimaticState = cinimaticState;
    this.frameCount = 0;
  }
  
  public void introCinematic(){
    frameCount++;
    if(frameCount < 100){
      text("You know the reason, it is well known", 100, 100);
    } else if(frameCount < 300){
      text("the counceler was old,\nthe world had worn him down", 100, 100);
    }
    else if(frameCount < 600){
      text("men with vested interests\n had whispered in his ear.\nThey had grown rich but\n the city had decayed", 100, 100);
    }
    else if(frameCount > 600){
      game.switchToBoardView();
    }
  }
  
  public void drawCinimatic(){
    for (Button button : buttons) {
      button.drawButton();
    }
    switch(cinimaticState){
     case INTRO_CINIMATIC: {
       this.introCinematic();
     }
       
    }
  }

  public void detectClick() {
    for (Button b : buttons) {
      b.detectClick();
    }
  }

  public void skip(){
    frameCount = 9999999;
  }

}
class Combat{
	private Player player;
	private PImage playerImage;
 
	private MonsterCard monster;
	private PImage monsterImage;
	private ArrayList<Button> itemButtons;

	private int timeTillMonsterAttacks;
	private int timeTillAttackEnds;
 
	private boolean hit;
	private boolean monsterHit;


	public Combat(Player player, MonsterCard monster){
		timeTillMonsterAttacks = 0;
		timeTillAttackEnds = 0;
		this.player = player;
		this.monster = monster;
		this.playerImage = player.getPlayerImage();
		this.monsterImage = monster.getMonsterImage();
		itemButtons = new ArrayList<Button>();

		hit = false;
		monsterHit = false;

		createItemButtons();
	}

	private void createItemButtons(){
		int y = 200;
		int id = 0;
		for(Item i : player.getItems()){
			//text(i.getDescription(), 500, y);
			itemButtons.add(new Button(i.getDescription(), ButtonAction.USE_ITEM, 500, y, 130, 32, id));
     		y+=50;
     		id++;
		}
	}

	public void useItem(int id){
		if(player.itemFinished(id)) return;
		
		println(player.getItems().get(id).getDescription());
		//inflict damage
		monster.inflictDamage(player.getItems().get(id).getAttack());
		//inflict modifiers
		monster.modifySpeed(player.getItems().get(id).getSpeed());
		monster.modifyDefense(player.getItems().get(id).getDefense());
		//heal
		player.healDamage(player.getItems().get(id).getHeal());

		player.usedItem(id);

		if(player.itemFinished(id))
			itemButtons.get(id).greyOut();
	}

	private void monsterAttack(){
		if(hit) return;
		if(monster.getHealth() < 1){
			monsterHit = true;
			return;
		}
		if(monster.getSpeed() < 1){
			return;
		}
		if(monster.getDefense()<1){
			println("monster should be pushed back");
		}
		hit = true;
		player.inflictDamage(monster.getAttack());
	}

	public void drawCombat(){
		timeTillMonsterAttacks++;
		timeTillAttackEnds++;
		image(playerImage, 400, 400);
		textSize(32);
		text("Health " + (monster.getHealth()) + 
			" Attack " + monster.getAttack() + 
			" Defense " + monster.getDefense() +
			" Speed " + monster.getSpeed(), 50, 50);
		text("Health " + (player.getHealth()), 600, 600);
		image(monsterImage, 200, 200);
		for(Button b : itemButtons){
			b.drawButton();
		}

		if(timeTillMonsterAttacks == 300){
			monsterAttack();
		}
		if(timeTillAttackEnds==400){
			finishAttack();
		}

		if(hit){
			//attack animation will go in here
			fill(255, 0, 0);
			textSize(150);
			text("X", 400, 400);
		}
		if(monsterHit){
			fill(255, 0, 0);
			textSize(150);
			text("X", 200, 200);
		}
		
	}

	public void finishAttack(){
		player.removeEmptyItems();
		monster.resetModifiers();
		game.board.isMonsterDead();
		game.finishAttack();
	}

	public void detectClick(){
		for(Button b : itemButtons){
			b.detectClick();
		}
	}
}
private enum GameViewState {
  BOARD, CINIMATIC, COMBAT
}

public class Game {
  Board board;
  GameViewState gameViewState;
  Cinimatic cinimatic;

  Combat combat;
  
  public Board getBoard(){
    return this.board;
  }

  public Game() {
    gameViewState = GameViewState.CINIMATIC;
    cinimatic = new Cinimatic(CinimaticState.INTRO_CINIMATIC);
    board = new Board();
    loading = false;
  }

  public void switchToBoardView(){
    this.gameViewState = GameViewState.BOARD;

  }

  public void enterCombat(int id){
    combat = new Combat(board.player, board.player.getPlayersTile().getMonsterAt(id));
    gameViewState = GameViewState.COMBAT;
  }

  public void finishAttack(){
    
    switchToBoardView();
    combat = null;
  }

  public void drawGame() {
    background(250);
    switch(gameViewState) {
    case CINIMATIC:
      cinimatic.drawCinimatic();
      break;
    case BOARD: 
      board.drawBoard();
      break;
    case COMBAT: 
     // try{
        combat.drawCombat();
     // } catch(Exception e){
       // println("combat possibly not loaded yet");
      //}
      break;
    }
  }

  public void detectClick() {
    switch(gameViewState) {
    case CINIMATIC: 
      cinimatic.detectClick();
      break;
    case BOARD:
      board.detectClick();
      break;
    case COMBAT:
      combat.detectClick();
      break;
    }
  }
}
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
class ButtonImageContainer{
	PImage png;
	String buttonText;

	public ButtonImageContainer(PImage png, String buttonText){
		this.png = png;
		this.buttonText = buttonText;
	}

	public PImage getPng(){
		return png;
	}
	public String getButtonText(){
		return buttonText;
	}
}

class ImageLoader{
	
	ArrayList<ButtonImageContainer> loadedImages;

	public ImageLoader(){
		loadedImages = new ArrayList<ButtonImageContainer>();
		loadImages();
	}
	
	public void loadImages(){
		loadedImages.add(new ButtonImageContainer(loadImage("start_game_button.png"), "Start Game"));
	}

	public ArrayList<ButtonImageContainer> getImages(){
		return loadedImages;
	}
}
class Item{
  
  private String description;
  private String text;
  
  private int attack;
  private int speed;
  private int defense;
  private int heal;

  private int uses;
  private int used;


  public Item(String description, String text, int attack, int speed, int defense, int heal, int uses){
    this.description = description;
    this.text = text;
    this.attack = attack;
    this.speed = speed;
    this.defense = defense;
    this.heal = heal;
    this.uses = uses;
    this.used = 0;
  }

  public int getUses(){
    return uses - used;
  }


  public void itemWasUsed(){
    used++;
  }

  public int getUsed(){
    return used;
  }


  public String getDescription(){
  	return description;
  }

  public int getAttack(){
    return this.attack;
  }
  public int getSpeed(){
    return this.speed;
  }
  public int getDefense(){
    return this.defense;
  }
  public int getHeal(){
    return this.heal;
  }
}
class ItemCard extends Card{
  private ArrayList<Item> items;

  ArrayList<Button> tileCardButtons;
  
  public ItemCard(String id, String order, String text, ArrayList<Item> items){
    super(id, order, text);

    this.items = items;
    tileCardButtons = new ArrayList<Button>();
    int count = 30;
    int cardId = 0;
    for(Item ci : items){
    	tileCardButtons.add(new Button(ci.getDescription(), ButtonAction.CARD_ITEM, 350, 300+count, 130, 32, cardId));
    	count+=30;
    	cardId++;
    }
  }

  //typicaly called by board to  
  //addCardItem to player
  public Item getItem(int itemId){
  	return items.get(itemId);
  }

  public ArrayList<Item> getItems(){
  	return items;
  }

  public void drawCard(){
    super.drawCard();

  	for(Button b : tileCardButtons){
  		b.drawButton();
  	}
  }

  public void detectClick(){
  	for(Button b : tileCardButtons){
  		b.detectClick();
  	}
  }

}
public class Menu{
  /*
  /////////YOU MAY BE REMOVED
  class Walker {
    int x, y;
    Walker() {
      x = width/2;
      y = height/2;
    }
    
    void display() {
      stroke(0);
      point(x,y);
    }
    
    void step() {
      int choice = int (random(4));
      switch(choice){
       case 0: x++; break;
       case 1: x--; break;
       case 2: y++; break;
       case 3: y--; break;
      }
    }
  }
  
  class WalkerTwo {
    float x, y;
    WalkerTwo() {
      x = width/2;
      y = height/2;
    }
    
    void display() {
      stroke(0, 255, 0);
      point(x,y);
    }
    
    void step() {
      float stepx = random(-1, 1);
      float stepy = random(-1, 1);
      x+= stepx;
      y+=stepy;
    }
  }
  */
  //END YOU MAY BE REMOVED
  
  ArrayList<Button> buttons;
  PImage titleImage;
  int counter;
  //Walker w;
  //WalkerTwo w2;
  
  public Menu() {
    counter = 0;
    buttons = new ArrayList<Button>();
    buttons.add(new Button("Start Game", ButtonAction.START_GAME, 100, 100, 130, 32));
    titleImage = loadImage("title.png");
    
   // w = new Walker();
   // w2 = new WalkerTwo();
    
    image(titleImage, width/4, height/5);
    for(Button button : buttons){
      button.drawButton();
    }
   
  }
  
  public void detectClick(){
    for(Button b : buttons){
     b.detectClick();
    }
  }
  
  public void drawMenu(){
    //w2.step();
    //w2.display();
    //w.step();
    //w.display();
    counter++;
  pushMatrix();
  translate(titleImage.X, titleImage.Y);
  translate(width/2, height/2);
  rotate(counter*TWO_PI/360);
  translate(-titleImage.width/2, -titleImage.height/2);

  popMatrix();
   
  }
}
class MonsterCard extends Card{
	private Button okButton;
	private int health;
	private int attack;
	private int defense;
	private int speed;

	int speedModifier;
	int defenseModifier;
	int attackModifier;



	private int damage;

	private PImage monsterImage;

	public int getHealth(){
    	return health-damage;
  	}
  	public int getAttack(){
  	  	return attack-attackModifier;
  	}
  	public int getDefense(){
  	  	return defense-defenseModifier;
  	}
  	public int getSpeed(){
  		return speed-speedModifier;
  	}
  	public void modifySpeed(int modSpeed){
  		speedModifier+=modSpeed;
  	}
  	public void modifyDefense(int modDef){
  		defenseModifier+=modDef;
  	}
  	public void modifyAttack(int modAttack){
  		attackModifier+=modAttack;
  	}

  	public void inflictDamage(int damage){
  	  	this.damage += damage;
  	}
  	public void healDamage(int damage){
  	  	this.damage -= damage;
  	}

  	public void resetModifiers(){
  		this.speedModifier = 0;
		this.defenseModifier = 0;
		this.attackModifier = 0;
  	}

  	public boolean isDead(){
  		return getHealth() < 1;
  	}

	public MonsterCard(String id, String order, String text, int health, int attack, int defense, int speed){
		super(id, order, text);
		okButton = new Button("OK", ButtonAction.MONSTER_OK, 450, 450, 130, 32);
		this.health = health;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		//modifieres
		this.damage = 0;
		this.speedModifier = 0;
		this.defenseModifier = 0;
		this.attackModifier = 0;
		//TODO: pimage location is to be defined in the xml data
		monsterImage = loadImage("monster.png");
	}

	//this is for drawing the card you draw
	public void drawCard(){
		super.drawCard();
		okButton.drawButton();
	}

	//this is for MonsterPosition to draw the image
	public PImage getMonsterImage(){
		return monsterImage;
	}

	public void detectClick(){
		okButton.detectClick();
	}
}
class MonsterPosition{
  private MonsterCard monsterCard;
  private boolean showButtons;
  private ArrayList<Button> buttons;
  private int id;
  private int x;
  private int y;
  private int sizeWidth;
  private int sizeHeight;
  private int size;
  
  public MonsterPosition(int id, int x, int y, int size){
    this.id = id;
    this.x = x;
    this.y = y;
    this.size = size;

    //TODO: FIX THIS UP
    this.sizeWidth = 100;
    this.sizeHeight = 100;

    this.monsterCard = null;
    this.showButtons = false;
    this.buttons = new ArrayList<Button>();
    this.buttons.add(new Button("View", ButtonAction.MONSTER_VIEW, x-100, y+50, 130, 32, id));
    this.buttons.add(new Button("Attack", ButtonAction.ATTACK_MONSTER, x, y+50, 130, 32, id));
    this.buttons.add(new Button("Defend", ButtonAction.MONSTER_DEFEND, x+100, y+50, 130, 32, id));
  }

  public void checkForDead(){
    if(monsterCard==null) return;
    if(monsterCard.isDead()){
      emptyMonsterPosition();
    }
  }

  public void setMonsterCard(MonsterCard card){
    monsterCard = card;
  }
  
  public MonsterCard moveMonster(){
    showButtons = false;
    MonsterCard tempMonster = monsterCard;
    monsterCard = null;
    return tempMonster;
  }

  public MonsterCard getMonsterCard(){
    return monsterCard;
  }
  
  public MonsterCard swapMonsterCard(MonsterCard monsterCard){
    MonsterCard tempMonster = monsterCard;
    monsterCard = monsterCard;
    return tempMonster;
  }
  
  public void emptyMonsterPosition(){
    monsterCard = null;
  }
  
  public boolean isMonsterThere(){
    return monsterCard!=null;
  }

  public void drawMonsters(){
    if(!isMonsterThere())
      return;

    fill(0);
    image(monsterCard.getMonsterImage(), x+20, y-50, size, size);
    //rect(x+20, y-100, 50, 100);

    if(showButtons){
      for(Button b : buttons){
        b.drawButton();
      }
    }

  }

  private void toggleMonsterButtons(){
    showButtons = !showButtons;
  }

  private void monsterClicked(){
    if(mouseX >= x && mouseX <= x+sizeWidth && mouseY >= y-sizeHeight && mouseY <= y){
      toggleMonsterButtons();
    }
  }

  public void detectClick(){
    if(!(isMonsterThere()))
      return;

    if(showButtons){
      for(Button b : buttons){
        b.detectClick();
      }
    }

    monsterClicked();

  }

}
class Player{

  private PImage playerImage;

	private int health;
  private int damage;

  private int energy;

  private ArrayList<Item> items;

	//private CardItem head;
	//private CardItem hand;
	//private CardItem feet;

  private Tile playersTile;

  private Button energyButton;
  private boolean movementOptions;
  private ArrayList<Button> movementButtons;

  public int getHealth(){
    return health - damage;
  }
  
  public void inflictDamage(int damage){
    this.damage += damage;
  }
  public void healDamage(int damage){
    this.damage -= damage;
  }


	public Player(){
    this.health = 4;
   
    this.energy = 0;

    items = new ArrayList<Item>();

    playerImage = loadImage("player.png");

    energyButton = new Button("Move", ButtonAction.MOVE, 100, 100, 130, 32);
    movementOptions = false;    

    movementButtons = new ArrayList<Button>();
    movementButtons.add(new Button("North", ButtonAction.NORTH, 100, 150, 130, 32));
    movementButtons.add(new Button("East", ButtonAction.EAST, 200, 150, 130, 32));
    movementButtons.add(new Button("South", ButtonAction.SOUTH, 300, 150, 130, 32));
    movementButtons.add(new Button("West", ButtonAction.WEST, 400, 150, 130, 32));

  }

  public boolean itemFinished(int id){
    return items.get(id).getUses() < 1;
  }

  public void removeEmptyItems(){
    ArrayList<Item> itemsRemaining = new ArrayList<Item>();
    for(Item i : items){
      if(i.getUses()>0){
        itemsRemaining.add(i);
      }
    }
    items = itemsRemaining;
  }

  public void usedItem(int id){
    items.get(id).itemWasUsed();
  }

  public ArrayList<Item> getItems() {
    return items;
  }

  public void useMovementEnergy(){
    energy -= 4;
  }

  public void incrementEnergy(){
    energy++;
  }

  public void setPlayersTile(Tile tile){
    playersTile = tile; 
  }
  
  public Tile getPlayersTile(){
   return playersTile; 
  }

  public PImage getPlayerImage(){
    return playerImage;
  }

  public void addCardItem(Item item){
    items.add(item);
    println(item.description + " added to inventory");
  }



  public void drawPlayer(){
    fill(50);
    text("Health " + getHealth(), 600, 600);
    if(energy<4){
      text("Energy " + energy, 100, 100);
    } else {
      energyButton.drawButton();
    }

    if(movementOptions){
      for(Button b : movementButtons){
        b.drawButton();
      }
    }
    //draw player
    image(playerImage, 100, 450, 100, 400);
  }

  public void detectClick(){
    if(energy>3){
      energyButton.detectClick();
    }
    playersTile.detectClick();

    if(movementOptions){
      for(Button b : movementButtons){
        b.detectClick();
      }
    }
  }

  public void drawPlayerItems(){
    fill(100);
    rect(100, 100, 650, 400);
    fill(150);
    int y = 200;
    for(Item i : items){
      text(i.getDescription() + ": atk " + i.getAttack() + ", spd " + i.getSpeed() + ", def " + i.getDefense() + ", hl " + i.getHeal() + ", use " + i.getUses(), 100, y);
      y+=50;
    } 
  }

  public void toggleMovementOptions(){
    movementOptions = !movementOptions;
  }
  
}
class PlayerPosition{
  private int iPos;
  private int jPos;
  private int rows;
  private int columns;
  
  public PlayerPosition(int iPos, int jPos, int rows, int columns){
    this.rows = rows;
    this.columns = columns;
    this.iPos = iPos;
    this.jPos = jPos;
  }
  
  public int getJPos(){
    return this.jPos;
  }
  public int getIPos(){
    return this.iPos;
  }
  public boolean moveNorth(){
    if(iPos == 0){
     return false;
    }
    iPos--;
    return true;
  }
  public boolean moveSouth(){
    if(iPos == rows-1){
      return false;
    }
    iPos++;
    return true;
    
  }
  public boolean moveEast(){
    if(jPos == columns-1){
      return false;
    }
    jPos++;
    return true;
  }
  public boolean moveWest(){
    if(jPos == 0){
      return false;
    }
    jPos--;
    return true;    
  }

  
}
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "theBoardGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
