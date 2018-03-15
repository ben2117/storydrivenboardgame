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