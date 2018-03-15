class Item{
  
  private String description;
  private String text;
  
  private int attack;
  private int speed;
  private int defense;
  private int heal;

  private int uses;
  private int used;


  public Item(String description, String text, int attack, int speed, int defense, int heal, int uses){
    this.description = description;
    this.text = text;
    this.attack = attack;
    this.speed = speed;
    this.defense = defense;
    this.heal = heal;
    this.uses = uses;
    this.used = 0;
  }

  public int getUses(){
    return uses - used;
  }


  public void itemWasUsed(){
    used++;
  }

  public int getUsed(){
    return used;
  }


  public String getDescription(){
  	return description;
  }

  public int getAttack(){
    return this.attack;
  }
  public int getSpeed(){
    return this.speed;
  }
  public int getDefense(){
    return this.defense;
  }
  public int getHeal(){
    return this.heal;
  }
}