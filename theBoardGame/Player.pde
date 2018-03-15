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