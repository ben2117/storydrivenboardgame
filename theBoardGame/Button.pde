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