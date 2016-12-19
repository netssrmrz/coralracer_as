package rs.projecta.object;

public class Finish
implements Has_Position, Is_Drawable
{
  public android.graphics.Paint p;
  public org.jbox2d.dynamics.Body body;
  public rs.projecta.world.World world;

  public Finish(rs.projecta.world.World world, float x, float y)
  {
    org.jbox2d.dynamics.BodyDef body_def;
    org.jbox2d.dynamics.FixtureDef fix_def;

    this.world=world;

    body_def=new org.jbox2d.dynamics.BodyDef();
    body_def.type=org.jbox2d.dynamics.BodyType.DYNAMIC;
    body_def.position=new org.jbox2d.common.Vec2(
      x/this.world.phys_scale, y/this.world.phys_scale);
    body_def.angle=0;
    body_def.userData=this;
    body=world.phys_world.createBody(body_def);

    fix_def=new org.jbox2d.dynamics.FixtureDef();
    fix_def.shape=new org.jbox2d.collision.shapes.CircleShape();
    fix_def.shape.setRadius(50f/this.world.phys_scale);
    fix_def.density=1;
    fix_def.friction=0;
    fix_def.restitution=1;
    fix_def.isSensor=true;
    body.createFixture(fix_def);

    this.p = new android.graphics.Paint();
    this.p.setColor(0xff0000ff);
  }
  
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    c.drawCircle(0, 0, 50, this.p);
  }

  public float Get_X()
  {
    return rs.projecta.Util.Get_Transform(this.world, this.body, 
      rs.projecta.Util.TID_X);
  }

  public float Get_Y()
  {
    return rs.projecta.Util.Get_Transform(this.world, this.body, 
      rs.projecta.Util.TID_Y);
  }
  
  public void Set_X(float x)
  {
    rs.projecta.Util.Set_Transform(this.world, this.body, x, null, null);
  }

  public void Set_Y(float y)
  {
    rs.projecta.Util.Set_Transform(this.world, this.body, null, y, null);
  }
}
