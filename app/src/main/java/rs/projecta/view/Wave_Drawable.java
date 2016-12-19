package rs.projecta.view;

public class Wave_Drawable
extends android.graphics.drawable.Drawable
implements rs.projecta.object.Has_Position
{
  public static final int WAVES=30;
  public rs.projecta.object.Background_Waves[] bks;
  public float[] pos;
  public android.graphics.Paint p;
  public android.graphics.Matrix mat;
  public long last_time;
  public float text_size;
  public long min_elapsed;
  
  public Wave_Drawable()
  {
    int c;
    
    this.text_size=0;
    this.min_elapsed=999999999;
    this.last_time=System.nanoTime();
    
    this.pos=new float[2];
    this.pos[0]=1000;
    this.pos[1]=1000;
    
    this.bks=new rs.projecta.object.Background_Waves[WAVES];
    for (c=0; c<WAVES; c++)
      this.bks[c]=new rs.projecta.object.Background_Waves(
        this, (float)c/40f+1f, android.graphics.Color.rgb(
          0, 0, (int)( (256f/(float)WAVES) * (float)(WAVES-c) )));
    
    this.p=new android.graphics.Paint();
    this.p.setStyle(android.graphics.Paint.Style.STROKE);
    this.p.setStrokeWidth(2);
    this.p.setColor(0xffffff00);
    this.p.setTextSize(200);
    this.p.setTextAlign(android.graphics.Paint.Align.CENTER);
    this.p.setPathEffect(new android.graphics.DiscretePathEffect(10, 5));
    this.p.setAntiAlias(false);
    
    this.mat=new android.graphics.Matrix();
  }
  
  @Override
  public void draw(android.graphics.Canvas c)
  {
    long elapsed_time, now;
    
    if (this.text_size==0)
    {
      this.text_size=c.getWidth()/6f;
      this.p.setTextSize(this.text_size);
    }
    
    now=System.nanoTime();
    elapsed_time=(now-this.last_time)/1000000;
    this.last_time=now;
    
    this.mat.setRotate(0.02f*elapsed_time);
    this.mat.mapPoints(this.pos);

    c.drawColor(0xff000000);
    c.save();
    c.translate(c.getWidth()/2f-this.Get_X(), c.getHeight()/2f-this.Get_Y());
    
    for (rs.projecta.object.Background_Waves bk: this.bks)
      if (bk!=null)
        bk.Draw(null, c);
    
    c.drawText("Coral Racer", this.Get_X(), this.Get_Y(), this.p);
 
    c.restore();
    
    //android.util.Log.d("Wave_Drawable.draw()", "elapsed_time: "+elapsed_time);
    elapsed_time=System.nanoTime()-now;
    if (elapsed_time<this.min_elapsed)
      this.min_elapsed=elapsed_time;
  }

  @Override
  public void setAlpha(int p1)
  {
    // TODO: Implement this method
  }

  @Override
  public void setColorFilter(android.graphics.ColorFilter p1)
  {
    // TODO: Implement this method
  }

  @Override
  public int getOpacity()
  {
    // TODO: Implement this method
    return 0;
  }
  
  @Override
  public float Get_X()
  {
    return this.pos[0];
  }

  @Override
  public float Get_Y()
  {
    return this.pos[1];
  }

  @Override
  public void Set_X(float x)
  {
    this.pos[0]=x;
  }

  @Override
  public void Set_Y(float y)
  {
    this.pos[1]=y;
  }
}
