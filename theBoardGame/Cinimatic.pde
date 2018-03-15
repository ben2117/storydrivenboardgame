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