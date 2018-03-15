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