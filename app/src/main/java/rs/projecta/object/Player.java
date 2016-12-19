package rs.projecta.object;

public class Player
implements Is_Drawable, Has_Position, Has_Direction, Can_Collide,
  Has_Auto_Movement, Is_Drawable_OpenGL
{
	public android.graphics.Paint p;
  public org.jbox2d.dynamics.Body body;
  public rs.projecta.world.World w;
  public android.graphics.Path[] frames;
  public float frame, frame_delta, frame_max;
  public final float size=24f;
  public final float trgt_v=80;
  public final float tilt_factor=40;
  public final float f=80;
  public java.nio.FloatBuffer b;
  public int OGL_POINT_COUNT=12;
  public float red, green, blue, alpha;

	public Player(float x, float y, rs.projecta.world.World world)
	{
    org.jbox2d.dynamics.BodyDef body_def;
    org.jbox2d.dynamics.FixtureDef fix_def;

    this.w = world;

    body_def = new org.jbox2d.dynamics.BodyDef();
    body_def.type = org.jbox2d.dynamics.BodyType.DYNAMIC;
    body_def.position = new org.jbox2d.common.Vec2(
      x / this.w.phys_scale, y / this.w.phys_scale);
    body_def.angle = 0;
    body_def.userData = this;
    body = world.phys_world.createBody(body_def);

    fix_def = new org.jbox2d.dynamics.FixtureDef();
    fix_def.shape = new org.jbox2d.collision.shapes.CircleShape();
    fix_def.shape.setRadius(this.size / this.w.phys_scale);
    fix_def.density = 0.08f;
    fix_def.friction = 0;
    fix_def.restitution = 1;
    fix_def.filter.groupIndex=-1;
    body.createFixture(fix_def);

    this.p = new android.graphics.Paint();
    this.p.setColor(0xffffffff);
    this.p.setAntiAlias(false);
    this.p.setStyle(android.graphics.Paint.Style.STROKE);
    
    this.frame=0;
    this.frame_delta=.01f;
    this.frame_max=4;
    this.frames=new android.graphics.Path[(int)this.frame_max];
    
    this.frames[0]=new android.graphics.Path();
    this.frames[0].moveTo(0,-15);
    this.frames[0].lineTo(5,-5);
    this.frames[0].lineTo(10,0);
    this.frames[0].lineTo(5,0);
    this.frames[0].lineTo(0,15);
    this.frames[0].lineTo(5,20);
    this.frames[0].lineTo(-5,20);
    this.frames[0].lineTo(0,15);
    this.frames[0].lineTo(-5,0);
    this.frames[0].lineTo(-10,0);
    this.frames[0].lineTo(-5,-5);
    this.frames[0].lineTo(0,-15);
    
    this.frames[1]=new android.graphics.Path();
    this.frames[1].moveTo(5,-15);
    this.frames[1].lineTo(5,-5);
    this.frames[1].lineTo(10,0);
    this.frames[1].lineTo(5,0);
    this.frames[1].lineTo(5,15);
    this.frames[1].lineTo(10,20);
    this.frames[1].lineTo(5,25);
    this.frames[1].lineTo(5,15);
    this.frames[1].lineTo(-5,0);
    this.frames[1].lineTo(-10,0);
    this.frames[1].lineTo(-5,-5);
    this.frames[1].lineTo(5,-15);
    
    this.frames[2]=new android.graphics.Path();
    this.frames[2].moveTo(0,-15);
    this.frames[2].lineTo(5,-5);
    this.frames[2].lineTo(10,0);
    this.frames[2].lineTo(5,0);
    this.frames[2].lineTo(0,15);
    this.frames[2].lineTo(5,20);
    this.frames[2].lineTo(-5,20);
    this.frames[2].lineTo(0,15);
    this.frames[2].lineTo(-5,0);
    this.frames[2].lineTo(-10,0);
    this.frames[2].lineTo(-5,-5);
    this.frames[2].lineTo(0,-15);
    
    this.frames[3]=new android.graphics.Path();
    this.frames[3].moveTo(-5,-15);
    this.frames[3].lineTo(-5,-5);
    this.frames[3].lineTo(-10,0);
    this.frames[3].lineTo(-5,0);
    this.frames[3].lineTo(-5,15);
    this.frames[3].lineTo(-10,20);
    this.frames[3].lineTo(-5,25);
    this.frames[3].lineTo(-5,15);
    this.frames[3].lineTo(5,0);
    this.frames[3].lineTo(10,0);
    this.frames[3].lineTo(5,-5);
    this.frames[3].lineTo(-5,-15);
    
    if (this.w.sounds!=null)
      this.w.sounds.play(this.w.soundid_start, 1, 1, 0, 0, 1);
      
    this.Init_OpenGL(0xffffffff);
  }

  public void Init_OpenGL(int col)
  {    
    float[] points;

    points=this.Get_Points();
    b=java.nio.ByteBuffer.allocateDirect(points.length*4)
      .order(java.nio.ByteOrder.nativeOrder())
      .asFloatBuffer();
    b.put(points);  
    b.position(0);

    this.red=(float)android.graphics.Color.red(col)/255f;
    this.green=(float)android.graphics.Color.green(col)/255f;
    this.blue=(float)android.graphics.Color.blue(col)/255f;
    this.alpha=(float)android.graphics.Color.alpha(col)/255f;
  }

  public float[] Get_Points()
  {
    float[] points=
    {
      0,-15, 5,-5, 10,0, 5,0, 0,15,
      5,20, -5,20, 0,15, -5,0, -10,0,
      -5,-5, 0,-15,
    
      5,-15, 5,-5, 10,0, 5,0, 5,15, 
      10,20, 5,25, 5,15, -5,0, -10,0,
      -5,-5, 5,-15,
   
      0,-15, 5,-5, 10,0, 5,0, 0,15,
      5,20, -5,20, 0,15, -5,0, -10,0,
      -5,-5, 0,-15,

      -5,-15, -5,-5, -10,0, -5,0, -5,15,
      -10,20, -5,25, -5,15, 5,0, 10,0,
      5,-5, -5,-15
    };

    return points;
  }
  
	public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
	{
    this.frame=this.frame+this.frame_delta*((float)this.w.lapsed_time/1000000f);
    this.frame=this.frame%this.frame_max;
    
    c.save();
    c.scale(4f, 4f);
    c.drawPath(this.frames[(int)this.frame], p);
    c.restore();
  }

  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    //android.util.Log.d("Player.Draw_OpenGL()", "Entered");
    
    this.frame=this.frame+this.frame_delta*((float)this.w.lapsed_time/1000000f);
    this.frame=this.frame%this.frame_max;
    
    android.opengl.GLES20.glVertexAttribPointer(
      v.att_loc, 2, android.opengl.GLES20.GL_FLOAT, false, 0, b);
    android.opengl.GLES20.glEnableVertexAttribArray(v.att_loc);
    
    v.Save_Transform();
    android.opengl.Matrix.scaleM(v.proj, 0, 4f, 4f, 1f);
    
    android.opengl.GLES20.glUniformMatrix4fv(v.mat_loc, 1, false, v.proj, 0);
    android.opengl.GLES20.glUniform4f(
      v.col_loc, this.red, this.green, this.blue, this.alpha);
    android.opengl.GLES20.glDrawArrays(
      android.opengl.GLES20.GL_LINE_LOOP, 
      (int)this.frame*OGL_POINT_COUNT, OGL_POINT_COUNT);
    
    v.Restore_Transform();
  }
  
	public float Get_X()
	{
		return this.body.getPosition().x * this.w.phys_scale;
	}

	public float Get_Y()
	{
		return this.body.getPosition().y * this.w.phys_scale;
	}

  public void Set_X(float x)
  {
    rs.projecta.Util.Set_Transform(this.w, this.body, x, null, null);
  }

  public void Set_Y(float y)
  {
    rs.projecta.Util.Set_Transform(this.w, this.body, null, y, null);
  }

	public float Get_Angle_Degrees()
	{
		return (float)java.lang.Math.toDegrees(this.body.getAngle());
	}

  public void Set_Angle_Degrees(float a)
  {
    rs.projecta.Util.Set_Transform(this.w, this.body, null, null, a);
  }

  public void User_Action(float f, float t)
  {
  }

	public void Turn(float tilt)
	{
    float curr_v, trgt_v, f=0;

    curr_v = this.body.getAngularVelocity();
    trgt_v = tilt * tilt_factor;
    f = trgt_v - curr_v;
    this.body.applyTorque(f);
	}

  public void Accelerate(float tilt)
  {
    /*String str;
    float curr_v, trgt_v, f=0;

    Remove_Lat_Vel(this.body);
    
    curr_v=this.body.getLinearVelocity().length();
    trgt_v=100f;
    if (curr_v<trgt_v)
    {
      f=tilt*100f;
      Apply_Frwd_Force(this.body, f);
    }

    if (this.world.debug)
    {
      str =
        "\nForward Tilt: "+tilt+"\n"+
        "Current Velocity: " + curr_v + "\n" +
        "Target Velocity: " + trgt_v + "\n"+
        "Force: "+f;
      this.world.debug_msg += str;
    }*/
  }

  public void Apply_Frwd_Force(org.jbox2d.dynamics.Body body, float f)
  {
    org.jbox2d.common.Vec2 frwd_vec, body_frwd_vec;
    
    frwd_vec = new org.jbox2d.common.Vec2(0, -f);
    body_frwd_vec = this.body.getWorldVector(frwd_vec);
    body.applyForceToCenter(body_frwd_vec);
  }
  
  public void Remove_Lat_Vel(org.jbox2d.dynamics.Body body)
  {
    org.jbox2d.common.Vec2 lat_vel, lat_impulse;
    
    lat_vel = Calc_Lat_Vel(body);
    lat_impulse = lat_vel.mul(-body.getMass());
    body.applyLinearImpulse(lat_impulse, body.getWorldCenter());
  }
  
  public org.jbox2d.common.Vec2 Calc_Lat_Vel(org.jbox2d.dynamics.Body body)
  {
    org.jbox2d.common.Vec2 res=null;
    org.jbox2d.common.Vec2 lat_vec, vel;
    float lat_vel;

    vel = this.body.getLinearVelocity();
    lat_vec = this.body.getWorldVector(new org.jbox2d.common.Vec2(1, 0));
    lat_vel = org.jbox2d.common.Vec2.dot(lat_vec, vel);
    res = lat_vec.mul(lat_vel);

    return res;
  }

  public void Contact(org.jbox2d.dynamics.contacts.Contact c)
  {
    Object a, b;

    a = c.getFixtureA().getBody().getUserData();
    b = c.getFixtureB().getBody().getUserData();

    if ((a != null && a instanceof Finish) || (b != null && b instanceof Finish))
    {
      this.w.Signal_End(rs.projecta.world.World.STATE_LEVELCOMPLETE);
    }
    
    if ((a != null && a instanceof Wall) || (b != null && b instanceof Wall))
    {
      Explosion.Add(this.w, this.Get_X(), this.Get_Y());
    }
    
    /*String m=null;
    if (a!=null) m=rs.android.Util.AppendStr(m, a.getClass().getName(), ", ");
    if (b!=null) m=rs.android.Util.AppendStr(m, b.getClass().getName(), ", ");
    android.util.Log.d("projecta", "contact between "+m);*/
  }
  
  public void Update(long dt)
  {
    float curr_v;
    org.jbox2d.common.Vec2 frwd_vec, body_frwd_vec, body_vel_vec;

    Remove_Lat_Vel(this.body);
    
    frwd_vec = new org.jbox2d.common.Vec2(0, -1);
    body_frwd_vec = this.body.getWorldVector(frwd_vec);
    body_vel_vec = this.body.getLinearVelocity();
    curr_v = org.jbox2d.common.Vec2.dot(body_vel_vec, body_frwd_vec);
    
    if (curr_v<trgt_v)
    {
      Apply_Frwd_Force(this.body, f);
    } 
  }
}
