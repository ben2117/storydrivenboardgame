class PlayerPosition{
  private int iPos;
  private int jPos;
  private int rows;
  private int columns;
  
  public PlayerPosition(int iPos, int jPos, int rows, int columns){
    this.rows = rows;
    this.columns = columns;
    this.iPos = iPos;
    this.jPos = jPos;
  }
  
  public int getJPos(){
    return this.jPos;
  }
  public int getIPos(){
    return this.iPos;
  }
  public boolean moveNorth(){
    if(iPos == 0){
     return false;
    }
    iPos--;
    return true;
  }
  public boolean moveSouth(){
    if(iPos == rows-1){
      return false;
    }
    iPos++;
    return true;
    
  }
  public boolean moveEast(){
    if(jPos == columns-1){
      return false;
    }
    jPos++;
    return true;
  }
  public boolean moveWest(){
    if(jPos == 0){
      return false;
    }
    jPos--;
    return true;    
  }

  
}