package rs.projecta.object;

public class Background_Grid
implements Is_Drawable
{
	public static final float TILE_SIZE=160;
	
	public android.graphics.Paint p;
  //public Object camera;

	public Background_Grid()
	{
		this.p = new android.graphics.Paint();
		this.p.setColor(0xff006600);
    
    //this.camera=camera;
	}
  
	public void Draw(rs.projecta.view.World_View v, 
    android.graphics.Canvas c)
	{
    float r, TILE_SIZE=50, x1, y1, x2, y2, x3, y3, x=0, y=0;

    r=Distance(0, 0, c.getWidth(), c.getHeight())/2f;
    
    if (v.camera!=null && 
      v.camera instanceof rs.projecta.object.Has_Position)
    {
      x=((rs.projecta.object.Has_Position)v.camera).Get_X();
      y=((rs.projecta.object.Has_Position)v.camera).Get_Y();
    }
    
    x1=x-r;
    y1=y-r;
    x2=x+r;
    y2=y+r;

    for (x=x1; x<x2; x+=TILE_SIZE)
    {
      x3=((int)(x/TILE_SIZE))*TILE_SIZE;
      c.drawLine(x3, y1, x3, y2, p);
    }
    for (y=y1; y<y2; y+=TILE_SIZE)
    {
      y3=((int)(y/TILE_SIZE))*TILE_SIZE;
      c.drawLine(x1, y3, x2, y3, p);
    }
	}
  
	public static float Distance(float x1, float y1, float x2, float y2)
  {
    return (float)Math.sqrt(Math.pow(x2-x1, 2)+Math.pow(y2-y1, 2));
  }
}
