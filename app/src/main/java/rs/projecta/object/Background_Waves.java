package rs.projecta.object;

public class Background_Waves
implements Is_Drawable, Is_Drawable_OpenGL
{
  public static final int PTS=20;
  public Has_Position cam;
  public android.graphics.Paint paint;
  public int tile_span;
  public float tile_size, tile_z, size_z;
  public android.graphics.Point curr_tile_index, curr_paint_index;
  android.graphics.PointF curr_tile_pos;
  public float cam_x, cam_y;
  public android.graphics.Path pts;
  public java.nio.FloatBuffer b;
  public int OGL_POINT_COUNT;
  public float red, green, blue, alpha;
  
  public Background_Waves(Has_Position cam, float height, int col)
  { 
    int c;
    
    curr_tile_index=new android.graphics.Point();
    curr_tile_pos=new android.graphics.PointF();
    curr_paint_index=new android.graphics.Point();
    this.cam=cam;
    this.tile_span=3;
    this.tile_z=height;
    this.tile_size=1000;
    size_z=tile_size/tile_z;
    
    this.paint=new android.graphics.Paint();
    this.paint.setStyle(android.graphics.Paint.Style.STROKE);
    this.paint.setColor(col);
    this.paint.setAntiAlias(false);
    
    this.pts=new android.graphics.Path();
    this.pts.moveTo(0, size_z/2f);
    for (c=0; c<PTS; c++)
    {
      this.pts.lineTo(
        size_z/PTS*(c+1),
        (float)java.lang.Math.sin(java.lang.Math.PI*2f/PTS*(c+1))*(size_z/4f)+(size_z/2f));
    }
    
    this.Init_OpenGL(col);
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
    float[] points;
    int c;
    
    points=new float[(PTS+1)*2];
    for (c=0; c<=PTS; c++)
    {
      points[c*2]=(size_z/PTS)*c;
      points[c*2+1]=
        (float)java.lang.Math.sin((java.lang.Math.PI*2f/PTS)*c)*(size_z/4f)+(size_z/2f);
    }
    this.OGL_POINT_COUNT=PTS+1;
    
    return points;
  }
  
  public void Render(android.view.View v, android.graphics.Canvas c)
  {    
    float tx, ty;

    this.cam_x=cam.Get_X();
    this.cam_y=cam.Get_Y();

    for (curr_tile_index.y=0; curr_tile_index.y<this.tile_span+1; curr_tile_index.y++)
    {
      for (curr_tile_index.x=0; curr_tile_index.x<this.tile_span+1; curr_tile_index.x++)
      {
        tx=cam_x+tile_size*(curr_tile_index.x-tile_span/2f);
        ty=cam_y+tile_size*(curr_tile_index.y-tile_span/2f);
        curr_tile_pos.x=(float)java.lang.Math.floor(tx/tile_size)*tile_size;
        curr_tile_pos.y=(float)java.lang.Math.floor(ty/tile_size)*tile_size;
        curr_tile_pos.x = (curr_tile_pos.x-cam_x) / tile_z + cam_x;
        curr_tile_pos.y = (curr_tile_pos.y-cam_y) / tile_z + cam_y;

        if (c!=null)
          Render_Canvas((rs.projecta.view.World_View)v, c);
        else
          Render_OpenGL((rs.projecta.view.Game2_View)v);
      }
    }
  }
  
  public void Render_Canvas(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    c.save();
    c.translate(curr_tile_pos.x, curr_tile_pos.y);
    c.drawPath(pts, this.paint);
    c.restore();
  }
  
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {   
    this.Render(v, c);
  }

  @Override
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    this.Render(v, null);
  }
  
  public void Render_OpenGL(rs.projecta.view.Game2_View v)
  {
    android.opengl.GLES20.glVertexAttribPointer(
      v.att_loc, 2, android.opengl.GLES20.GL_FLOAT, false, 0, b);
    android.opengl.GLES20.glEnableVertexAttribArray(v.att_loc);

    v.Save_Transform();
    android.opengl.Matrix.translateM(
      v.proj, 0, curr_tile_pos.x, curr_tile_pos.y, 0);

    android.opengl.GLES20.glUniformMatrix4fv(v.mat_loc, 1, false, v.proj, 0);
    android.opengl.GLES20.glUniform4f(
      v.col_loc, this.red, this.green, this.blue, this.alpha);
    android.opengl.GLES20.glDrawArrays(
      android.opengl.GLES20.GL_LINE_STRIP, 0, OGL_POINT_COUNT);

    v.Restore_Transform();
  }
  
  public static void Select_Tile_To_Render(
    Has_Position cam, android.graphics.Point curr_tile_index,
    float tile_width, int tile_span,
    android.graphics.Point curr_paint_index)
  {
    float tx, ty;
    
    tx=cam.Get_X()/tile_width-(float)tile_span/2f+curr_tile_index.x;
    ty=cam.Get_Y()/tile_width-(float)tile_span/2f+curr_tile_index.y;
    curr_paint_index.x=(int)java.lang.Math.abs(java.lang.Math.floor(tx)%tile_span);
    curr_paint_index.y=(int)java.lang.Math.abs(java.lang.Math.floor(ty)%tile_span);
  }
}
