package rs.projecta;

public class Util
{
  public static int TID_X=1;
  public static int TID_Y=2;
  public static int TID_A=3;
  
  public static void Set_Transform(
    rs.projecta.world.World world, org.jbox2d.dynamics.Body body,
    Float x, Float y, Float a)
  {
    org.jbox2d.common.Vec2 curr_pos;

    curr_pos=body.getPosition();
    if (x==null)
      x=curr_pos.x;
    else
      x=x/world.phys_scale;
    if (y==null)
      y=curr_pos.y;
    else
      y=y/world.phys_scale;
    
    if (a!=null)
      a=(float)java.lang.Math.toRadians(a);
    else
      a=body.getAngle();

    body.setTransform(new org.jbox2d.common.Vec2(x, y), a);
  }
  
  public static float Get_Transform(
    rs.projecta.world.World world, org.jbox2d.dynamics.Body body, int id)
  {
    float res=0;
    
    if (id==TID_X)
      res = body.getPosition().x*world.phys_scale;
    else if (id==TID_Y)
      res = body.getPosition().y*world.phys_scale;
    else if (id==TID_A)
      res = (float)java.lang.Math.toDegrees(body.getAngle());
      
    return res;
  }
}
