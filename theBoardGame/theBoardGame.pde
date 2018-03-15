public enum GameState { MENU, SETTING, GAME }

GameState gameState = GameState.MENU;

ActionHandler actionHandler;
Game game;
Menu menu;
GameLoader gameLoader;
ImageLoader imageLoader;
boolean loading;

ArrayList<Callback> callbacks = new ArrayList<Callback>();

void addCallback(String callFunction, int timer, Button button){
  callbacks.add(new Callback(callFunction, timer, button));

}

void settings() {
  //fullScreen(JAVA2D);
  //this fixes crash when not in landscape mode.
  orientation(LANDSCAPE);
  size(800, 600, JAVA2D);
}

void setup() {
   imageLoader = new ImageLoader();
   gameLoader = new GameLoader();
   menu = new Menu();
   actionHandler = new ActionHandler();
   imageMode(CENTER);
}

void draw() {
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

void mousePressed() {
  switch(gameState){
     case MENU: menu.detectClick(); break;
     case GAME: game.detectClick(); break;
     case SETTING: println("setting"); break;
  }
}