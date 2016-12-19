package rs.projecta.object;

public class Pointer
implements Is_Drawable, Has_Position, Has_Direction
{
  public rs.projecta.object.Has_Position src, trg;
  public android.graphics.Paint p;
  public rs.projecta.world.World world;

  public Pointer(rs.projecta.world.World world, 
    rs.projecta.object.Has_Position src, rs.projecta.object.Has_Position target)
  {
    this.world = world;
    this.src = src;
    this.trg = target;
    this.p = new android.graphics.Paint();
    this.p.setColor(0xff000088);
  }

  @Override
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    float tx, ty, x=0, y=0;

    /*tx=this.trg.Get_X();
     ty=this.trg.Get_Y();

     if (ty>Math.abs(tx)) // a
     {
     y=-(float)c.getHeight()/2f;
     x=(y*tx)/ty;
     }
     else if (Math.abs(ty)<tx) // b
     {

     }
     else if (ty<Math.abs(tx)) // c
     {

     }
     else if (Math.abs(ty)>tx) // d
     {

     }*/
    // y=mx+b
    // y=(dy/dx)x+b
    // y=(ty/tx)*x
    // ay=(ty/tx)*x
    // ay/(ty/tx)=x
    // (ay*tx)/ty=x

    c.drawLine(x, y + 200, x - 20, y - 80 + 200, this.p);
    c.drawLine(x - 20, y - 80 + 200, x + 20, y - 80 + 200, this.p);
    c.drawLine(x + 20, y - 80 + 200, x, y + 200, this.p);
  }

  public float Get_X()
  {
    return this.src.Get_X();
  }

  public float Get_Y()
  {
    return this.src.Get_Y();
  }
  
  public void Set_X(float x)
  {
  }

  public void Set_Y(float y)
  {
  }

  public float Get_Angle_Degrees()
  {
    float a, x, y;

    x = this.trg.Get_X() - this.src.Get_X();
    y = this.trg.Get_Y() - this.src.Get_Y();
    a = -(float)Math.toDegrees(org.jbox2d.common.MathUtils.atan2(x, y));

    return a;
  }
  
  public void Set_Angle_Degrees(float a)
  {
    
  }
}
