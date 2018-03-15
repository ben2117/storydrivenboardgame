class Combat{
	private Player player;
	private PImage playerImage;
 
	private MonsterCard monster;
	private PImage monsterImage;
	private ArrayList<Button> itemButtons;

	private int timeTillMonsterAttacks;
	private int timeTillAttackEnds;
 
	private boolean hit;
	private boolean monsterHit;


	public Combat(Player player, MonsterCard monster){
		timeTillMonsterAttacks = 0;
		timeTillAttackEnds = 0;
		this.player = player;
		this.monster = monster;
		this.playerImage = player.getPlayerImage();
		this.monsterImage = monster.getMonsterImage();
		itemButtons = new ArrayList<Button>();

		hit = false;
		monsterHit = false;

		createItemButtons();
	}

	private void createItemButtons(){
		int y = 200;
		int id = 0;
		for(Item i : player.getItems()){
			//text(i.getDescription(), 500, y);
			itemButtons.add(new Button(i.getDescription(), ButtonAction.USE_ITEM, 500, y, 130, 32, id));
     		y+=50;
     		id++;
		}
	}

	public void useItem(int id){
		if(player.itemFinished(id)) return;
		
		println(player.getItems().get(id).getDescription());
		//inflict damage
		monster.inflictDamage(player.getItems().get(id).getAttack());
		//inflict modifiers
		monster.modifySpeed(player.getItems().get(id).getSpeed());
		monster.modifyDefense(player.getItems().get(id).getDefense());
		//heal
		player.healDamage(player.getItems().get(id).getHeal());

		player.usedItem(id);

		if(player.itemFinished(id))
			itemButtons.get(id).greyOut();
	}

	private void monsterAttack(){
		if(hit) return;
		if(monster.getHealth() < 1){
			monsterHit = true;
			return;
		}
		if(monster.getSpeed() < 1){
			return;
		}
		if(monster.getDefense()<1){
			println("monster should be pushed back");
		}
		hit = true;
		player.inflictDamage(monster.getAttack());
	}

	public void drawCombat(){
		timeTillMonsterAttacks++;
		timeTillAttackEnds++;
		image(playerImage, 400, 400);
		textSize(32);
		text("Health " + (monster.getHealth()) + 
			" Attack " + monster.getAttack() + 
			" Defense " + monster.getDefense() +
			" Speed " + monster.getSpeed(), 50, 50);
		text("Health " + (player.getHealth()), 600, 600);
		image(monsterImage, 200, 200);
		for(Button b : itemButtons){
			b.drawButton();
		}

		if(timeTillMonsterAttacks == 300){
			monsterAttack();
		}
		if(timeTillAttackEnds==400){
			finishAttack();
		}

		if(hit){
			//attack animation will go in here
			fill(255, 0, 0);
			textSize(150);
			text("X", 400, 400);
		}
		if(monsterHit){
			fill(255, 0, 0);
			textSize(150);
			text("X", 200, 200);
		}
		
	}

	public void finishAttack(){
		player.removeEmptyItems();
		monster.resetModifiers();
		game.board.isMonsterDead();
		game.finishAttack();
	}

	public void detectClick(){
		for(Button b : itemButtons){
			b.detectClick();
		}
	}
}