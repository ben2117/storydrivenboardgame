class ButtonImageContainer{
	PImage png;
	String buttonText;

	public ButtonImageContainer(PImage png, String buttonText){
		this.png = png;
		this.buttonText = buttonText;
	}

	public PImage getPng(){
		return png;
	}
	public String getButtonText(){
		return buttonText;
	}
}

class ImageLoader{
	
	ArrayList<ButtonImageContainer> loadedImages;

	public ImageLoader(){
		loadedImages = new ArrayList<ButtonImageContainer>();
		loadImages();
	}
	
	public void loadImages(){
		loadedImages.add(new ButtonImageContainer(loadImage("start_game_button.png"), "Start Game"));
	}

	public ArrayList<ButtonImageContainer> getImages(){
		return loadedImages;
	}
}