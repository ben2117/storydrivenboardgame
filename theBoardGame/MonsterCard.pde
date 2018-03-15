class MonsterCard extends Card{
	private Button okButton;
	private int health;
	private int attack;
	private int defense;
	private int speed;

	int speedModifier;
	int defenseModifier;
	int attackModifier;



	private int damage;

	private PImage monsterImage;

	public int getHealth(){
    	return health-damage;
  	}
  	public int getAttack(){
  	  	return attack-attackModifier;
  	}
  	public int getDefense(){
  	  	return defense-defenseModifier;
  	}
  	public int getSpeed(){
  		return speed-speedModifier;
  	}
  	public void modifySpeed(int modSpeed){
  		speedModifier+=modSpeed;
  	}
  	public void modifyDefense(int modDef){
  		defenseModifier+=modDef;
  	}
  	public void modifyAttack(int modAttack){
  		attackModifier+=modAttack;
  	}

  	public void inflictDamage(int damage){
  	  	this.damage += damage;
  	}
  	public void healDamage(int damage){
  	  	this.damage -= damage;
  	}

  	public void resetModifiers(){
  		this.speedModifier = 0;
		this.defenseModifier = 0;
		this.attackModifier = 0;
  	}

  	public boolean isDead(){
  		return getHealth() < 1;
  	}

	public MonsterCard(String id, String order, String text, int health, int attack, int defense, int speed){
		super(id, order, text);
		okButton = new Button("OK", ButtonAction.MONSTER_OK, 450, 450, 130, 32);
		this.health = health;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		//modifieres
		this.damage = 0;
		this.speedModifier = 0;
		this.defenseModifier = 0;
		this.attackModifier = 0;
		//TODO: pimage location is to be defined in the xml data
		monsterImage = loadImage("monster.png");
	}

	//this is for drawing the card you draw
	public void drawCard(){
		super.drawCard();
		okButton.drawButton();
	}

	//this is for MonsterPosition to draw the image
	public PImage getMonsterImage(){
		return monsterImage;
	}

	public void detectClick(){
		okButton.detectClick();
	}
}
