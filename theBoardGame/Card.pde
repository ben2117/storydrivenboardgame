class Card{

  private String id;
  private String order;
  private ArrayList<String> styledText;
  private String text;

  public Card(){}

  public Card(String id, String order, String text){
  	this.id = id;
    this.order = order;
    try{
    	this.styledText = operateOnText(text);
    } catch(Exception e){
    	println("problem operating on text");
    }
    this.text = text;
  }

  public Item getItem(int id) {
    return null;
  }

  public void drawCard(){
  	fill(70);
  	rect(300, 100, 400, 400);
  	fill(200);
  	textSize(24);
  	int count = 30;
  	for(String t: styledText){
  		text(t, 350, 150+count);
  		count+=30;
  	}
  }

   public String getId(){
  	return id;
  }
  public String getText(){
	return text;
  }

  public void detectClick(){}

   private ArrayList<String> operateOnText(String text){
  	ArrayList<String> styledText = new ArrayList<String>();
  	String[] opT = split(text, ' ');
  	int lc = 0;
  	while(lc < opT.length){
  		String opO = "";
  		for(int i = 0; i < 4; i++){
  			opO = opO + opT[lc] + " ";
  			lc++;
  			if(lc == opT.length) break;
  		}
  		println(opO);
  		styledText.add(opO);
  	}
  	return styledText;
  }
}