class ItemCard extends Card{
  private ArrayList<Item> items;

  ArrayList<Button> tileCardButtons;
  
  public ItemCard(String id, String order, String text, ArrayList<Item> items){
    super(id, order, text);

    this.items = items;
    tileCardButtons = new ArrayList<Button>();
    int count = 30;
    int cardId = 0;
    for(Item ci : items){
    	tileCardButtons.add(new Button(ci.getDescription(), ButtonAction.CARD_ITEM, 350, 300+count, 130, 32, cardId));
    	count+=30;
    	cardId++;
    }
  }

  //typicaly called by board to  
  //addCardItem to player
  public Item getItem(int itemId){
  	return items.get(itemId);
  }

  public ArrayList<Item> getItems(){
  	return items;
  }

  public void drawCard(){
    super.drawCard();

  	for(Button b : tileCardButtons){
  		b.drawButton();
  	}
  }

  public void detectClick(){
  	for(Button b : tileCardButtons){
  		b.detectClick();
  	}
  }

}