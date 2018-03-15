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