package rs.projecta.object;

public class Wall
implements 
  Is_Drawable, Is_Drawable_OpenGL, Has_Position, Has_Direction, Has_Cleanup
{
  public org.jbox2d.dynamics.Body body;
  public rs.projecta.world.World world;
  public java.nio.FloatBuffer b;
  public int pt_count;
  public float red, green, blue, alpha;

  public Wall(rs.projecta.world.World world, float x, float y, float x1, float y1, float x2, float y2, float a_degrees)
  {
    //this.Init(world, x, y, x1, y1, x2, y2, a_degrees);
  }

  public Wall(rs.projecta.world.World world, org.jbox2d.common.Vec2 pos, float rx, float ry, float a_degrees)
  {
    this.Init(world, pos, rx, ry, a_degrees);
  }

  public Wall(rs.projecta.world.World world, float x, float y, float rx, float ry, float a_degrees)
  {
    //this.Init(world, pos, rx, ry, a_degrees);
  }

  public void Init
    (rs.projecta.world.World world, org.jbox2d.common.Vec2 pos, float rx, float ry, float a_degrees)
  {
    this.world=world;
    this.body=this.Init_Phys(pos, rx, ry, a_degrees);
    this.Init_OpenGL(rx, ry);
  }

  public org.jbox2d.dynamics.Body Init_Phys(org.jbox2d.common.Vec2 pos, float rx, float ry, float a)
  {
    org.jbox2d.dynamics.BodyDef body_def;
    org.jbox2d.dynamics.FixtureDef fix_def;
    org.jbox2d.collision.shapes.PolygonShape shape;
    org.jbox2d.dynamics.Body body;

    body_def=new org.jbox2d.dynamics.BodyDef();
    body_def.type=org.jbox2d.dynamics.BodyType.STATIC;
    body_def.position=world.To_Phys_Pt(pos);
    body_def.angle=(float)java.lang.Math.toRadians(a);
    body_def.userData=this;
    body=world.phys_world.createBody(body_def);

    shape=new org.jbox2d.collision.shapes.PolygonShape();
    shape.setAsBox(world.To_Phys_Dim(rx), world.To_Phys_Dim(ry), new org.jbox2d.common.Vec2(0, 0), 0);

    fix_def=new org.jbox2d.dynamics.FixtureDef();
    fix_def.shape=shape;
    fix_def.density=1;
    fix_def.friction=0;
    fix_def.restitution=2;
    body.createFixture(fix_def);

    return body;
  }

  public void Init_OpenGL(float rx, float ry)
  {    
    float[] points;
    
    points=this.Get_Points(rx, ry);
    pt_count=points.length/2;
    b=java.nio.ByteBuffer.allocateDirect(points.length*4)
      .order(java.nio.ByteOrder.nativeOrder())
      .asFloatBuffer();
    b.put(points);  
    b.position(0);

    this.red=(128f+(float)world.rnd.nextInt(128))/255f;
    this.green=(128f+(float)world.rnd.nextInt(128))/255f;
    this.blue=(128f+(float)world.rnd.nextInt(128))/255f;
    this.alpha=255f;
  }

  public float[] Get_Points(float rx, float ry)
  {
    float[] points;
    java.util.ArrayList<Float> pts;

    pts=new java.util.ArrayList<Float>();
    Line_Effect(-rx, -ry, -rx, +ry, pts);
    Line_Effect(-rx, +ry, +rx, +ry, pts);
    Line_Effect(+rx, +ry, +rx, -ry, pts);
    Line_Effect(+rx, -ry, -rx, -ry, pts);
    points=rs.android.util.Type.To_Float_Array(pts);
    
    return points;
  }
  
  public void Line_Effect(float x1, float y1, float x2, float y2, 
    java.util.ArrayList<Float> pts)
  {
    int c, seg_count=10;
    float x, y, pt[], d;
    java.util.Random rnd;
    
    rnd=new java.util.Random();
    pt=new float[2];
    
    d=Distance(x1, y1, x2, y2);
    seg_count=(int)(d/20f);
    if (seg_count<1)
      seg_count=1;
    
    for (c=0; c<seg_count; c++)
    {
      x=x1+((x2-x1)/(float)seg_count*(float)c);
      y=y1+((y2-y1)/(float)seg_count*(float)c);
      pt[0]=x; pt[1]=y;
      Deviate(pt, rnd, 10);
      pts.add(pt[0]);
      pts.add(pt[1]);
    }
  }
  
  public float[] Deviate(float[] pt, java.util.Random rnd, float dev)
  {
    pt[0]+=rnd.nextFloat()*2f*dev-dev;
    pt[1]+=rnd.nextFloat()*2f*dev-dev;
    return pt;
  }
  
  public float Distance(float x1, float y1, float x2, float y2)
  {
    double x, y, d;
    
    x=x2-x1;
    y=y2-y1;
    d=Math.sqrt(x*x+y*y);
    
    return (float)d;
  }
  
  @Override
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
  }

  @Override
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    android.opengl.GLES20.glVertexAttribPointer(v.att_loc, 2, android.opengl.GLES20.GL_FLOAT, false, 0, b);
    android.opengl.GLES20.glEnableVertexAttribArray(v.att_loc);
    
    android.opengl.GLES20.glUniformMatrix4fv(v.mat_loc, 1, false, v.proj, 0);
    android.opengl.GLES20.glUniform4f(v.col_loc, this.red, this.green, this.blue, this.alpha);
    android.opengl.GLES20.glDrawArrays(android.opengl.GLES20.GL_LINE_LOOP, 0, this.pt_count);
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
    org.jbox2d.common.Vec2 curr_pos, new_pos;
    float angle;
    
    curr_pos=this.body.getPosition();
    new_pos=new org.jbox2d.common.Vec2(x/world.phys_scale, curr_pos.y);
    angle=this.body.getAngle();
    
    this.body.setTransform(new_pos, angle);
  }

  public void Set_Y(float y)
  {
    org.jbox2d.common.Vec2 curr_pos, new_pos;
    float angle;

    curr_pos=this.body.getPosition();
    new_pos=new org.jbox2d.common.Vec2(curr_pos.x, y/world.phys_scale);
    angle=this.body.getAngle();

    this.body.setTransform(new_pos, angle);
  }
  
  public float Get_Angle_Degrees()
  {
    return (float)java.lang.Math.toDegrees(this.body.getAngle());
  }

  public void Set_Angle_Degrees(float a)
  {
    this.body.setTransform(this.body.getPosition(), 
      (float)java.lang.Math.toRadians(a));
  }

  public void Remove()
  {
    this.world.phys_world.destroyBody(this.body);
  }
}
