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