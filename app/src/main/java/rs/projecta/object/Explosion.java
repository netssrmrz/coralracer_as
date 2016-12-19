package rs.projecta.object;
//import rs.projecta.view.*;

public class Explosion
implements Is_Drawable, Has_Position, Is_Drawable_OpenGL
{
  public float cx, cy;
  public float r, r_delta, r_max;
  public rs.projecta.world.World w;
  //public android.graphics.Paint p;
  public java.nio.FloatBuffer b;
  public int pt_count;
  public float red, green, blue, alpha;
  
  public Explosion(rs.projecta.world.World w, float cx, float cy)
  {
    this.w=w;
    this.cx=cx;
    this.cy=cy;
    this.r_delta=3f;
    this.r_max=1500f;
    this.r=0;
    
    /*this.p=new android.graphics.Paint();
    this.p.setColor(0xffff00ff);
    this.p.setStyle(android.graphics.Paint.Style.STROKE);
    this.p.setAntiAlias(false);
    this.p.setPathEffect(new android.graphics.DiscretePathEffect(15, 90));*/
    
    if (this.w.sounds!=null)
      this.w.sounds.play(this.w.soundid_whack, 1, 1, 0, 0, 1);
      
    this.Init_OpenGL(0xffff00ff);
  }
  
  public void Init_OpenGL(int col)
  {    
    float[] points;

    points=this.Get_Points();
    pt_count=points.length/2;
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
    float a, s=1f;
    float[] p;
    int i;

    p=new float[40];
    for (i=0; i<p.length; i+=2)
    {
      if (s==1f)
        s=2f;
      else
        s=1f;
      
      a=(float)java.lang.Math.PI/((float)p.length/2f)*(float)i;
      p[i]=(float)java.lang.Math.cos(a)*s;
      p[i+1]=(float)java.lang.Math.sin(a)*s;
    }
    
    return p;
  }
  
  @Override
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    /*r=r+this.r_delta*((float)this.w.lapsed_time/1000000f);
    if (r>r_max)
    {
      w.objs.Remove(this);
      w.Signal_End(rs.projecta.world.World.STATE_LEVELFAIL);
    }
    else
    {
      c.drawCircle(0, 0, r, p);
    }*/
  }

  @Override
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    r=r+this.r_delta*((float)this.w.lapsed_time/1000000f);
    if (r>r_max)
    {
      w.objs.Remove(this);
      //w.Signal_End(rs.projecta.world.World.STATE_LEVELFAIL);
    }
    else
    {
      android.opengl.GLES20.glVertexAttribPointer(
        v.att_loc, 2, android.opengl.GLES20.GL_FLOAT, false, 0, b);
      android.opengl.GLES20.glEnableVertexAttribArray(v.att_loc);
      
      v.Save_Transform();
      android.opengl.Matrix.scaleM(v.proj, 0, r, r, 1f);
      
      android.opengl.GLES20.glUniformMatrix4fv(v.mat_loc, 1, false, v.proj, 0);
      android.opengl.GLES20.glUniform4f(
        v.col_loc, this.red, this.green, this.blue, this.alpha);
      android.opengl.GLES20.glDrawArrays(
        android.opengl.GLES20.GL_LINE_LOOP, 0, this.pt_count);
        
      v.Restore_Transform();
    }
  }
  
  @Override
  public float Get_X()
  {
    return this.cx;
  }

  @Override
  public float Get_Y()
  {
    return this.cy;
  }

  @Override
  public void Set_X(float x)
  {
    this.cx=x;
  }

  @Override
  public void Set_Y(float y)
  {
    this.cy=y;
  }
  
  public static void Add(rs.projecta.world.World w, float cx, float cy)
  {
    if (w.objs.Get_Count(Explosion.class)<3)
      w.objs.Add(new Explosion(w, cx, cy));
  }
}
