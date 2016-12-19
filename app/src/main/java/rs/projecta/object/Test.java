package rs.projecta.object;

public class Test
implements Is_Drawable ,Has_Position
{
  public org.jbox2d.dynamics.Body body;
  public android.graphics.Paint p;
  public rs.projecta.world.World world;
  public float radius;
  int frame;
  
  public Test(rs.projecta.world.World world)
  {
    org.jbox2d.dynamics.BodyDef body_def;
    org.jbox2d.dynamics.FixtureDef fix_def;
    org.jbox2d.collision.shapes.MassData md;
    float x, y, m;
    java.util.Random rnd;
    
    this.world=world;
    
    rnd=new java.util.Random();
    x=rnd.nextInt(500)-250;
    y=rnd.nextInt(500)-250;
    this.radius=rnd.nextInt(10)+5;
    m=this.radius*10f;
      
    body_def=new org.jbox2d.dynamics.BodyDef();
    body_def.type=org.jbox2d.dynamics.BodyType.DYNAMIC;
    body_def.position=new org.jbox2d.common.Vec2(
      x/world.phys_scale, y/world.phys_scale);
    body_def.angle=0;
    body=world.phys_world.createBody(body_def);

    fix_def=new org.jbox2d.dynamics.FixtureDef();
    fix_def.shape=new org.jbox2d.collision.shapes.CircleShape();
    fix_def.shape.setRadius(this.radius/world.phys_scale);
    fix_def.density=1;
    fix_def.friction=0;
    fix_def.restitution=1;
    body.createFixture(fix_def);
    
    md=new org.jbox2d.collision.shapes.MassData();
    md.mass=m;
    //body.setMassData(md);
    
    this.p = new android.graphics.Paint();
    this.p.setColor(0xffff0000);
    
    this.frame=rnd.nextInt(10);
  }

  @Override
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    c.drawCircle(0, 0, this.radius+(this.frame/2), p);
    
    this.frame=(this.frame+1) % 10;
  }
  
  public float Get_X()
  {
    return this.body.getPosition().x*this.world.phys_scale;
  }
  
	public float Get_Y()
  {
    return this.body.getPosition().y*this.world.phys_scale;
  }
  
  public void Set_X(float x)
  {
  }

  public void Set_Y(float y)
  {
  }
}
