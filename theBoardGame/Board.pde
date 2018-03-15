import java.util.Collections;

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
