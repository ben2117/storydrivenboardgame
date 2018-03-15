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