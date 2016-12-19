package rs.projecta.object;

public class Score
implements Is_Drawable, Is_Drawable_OpenGL
{
  public rs.projecta.world.World w;
  public android.graphics.Paint p, p1, p2;
  public float size, size_delta, size_max;
  public String text;
  public android.graphics.PointF pos;
  public Has_Position pos_obj;
  public android.graphics.Bitmap bitmap;
  public android.graphics.RectF dst_rect;
  public float digit_size=2.5f;
  public java.nio.FloatBuffer b;
  public int OGL_POINT_COUNT=12;
  public float red, green, blue, alpha;

  public Score(rs.projecta.world.World world, String text, Has_Position pos_obj)
  {
    this.w = world;
    this.size = 0;
    this.size_delta = 0.5f;
    this.size_max = 500;
    this.text = text;
    this.pos = new android.graphics.PointF();
    this.pos_obj = pos_obj;

    this.p = new android.graphics.Paint();
    this.p.setStyle(android.graphics.Paint.Style.STROKE);
    this.p.setStrokeWidth(2);
    this.p.setColor(0xff00ff00);
    this.p.setAntiAlias(false);
    this.p.setTextAlign(android.graphics.Paint.Align.CENTER);

    this.Init_OpenGL(0xff00ff00);
  }

  // bitmap
  /*public void xDraw(rs.projecta.view.World_View v, android.graphics.Canvas c)
   {
   float size=100;

   this.pos.x = this.pos_obj.Get_X();
   this.pos.y = this.pos_obj.Get_Y();
   size = 10f * (float)frame_no;

   dst_rect.left = pos.x-size;
   dst_rect.top = pos.y-size;
   dst_rect.right = pos.x+size;
   dst_rect.bottom = pos.y+size;
   c.drawBitmap(this.bitmap, null, dst_rect, null);

   this.frame_no++;
   if (this.frame_no>100)
   this.world.objs.Remove(this);
   }*/

  // canvas lines
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    this.size = this.size + this.size_delta * ((float)this.w.lapsed_time / 1000000f);
    if (this.size > this.size_max)
    {
      this.w.objs.Remove(this);
    }
    else
    {
      this.pos.x = this.pos_obj.Get_X();
      this.pos.y = this.pos_obj.Get_Y();

      pos = this.Calc_Text_Center(this.pos, this.p);
      this.Draw_Text(v, this.text, this.pos.x, this.pos.y, size, this.p);
    }
  }

  // canvas text
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    this.size = this.size + this.size_delta * ((float)this.w.lapsed_time / 1000000f);
    if (this.size > this.size_max)
    {
      this.w.objs.Remove(this);
    }
    else
    {
      this.pos.x = this.pos_obj.Get_X();
      this.pos.y = this.pos_obj.Get_Y();
      this.p.setTextSize(size);

      pos = this.Calc_Text_Center(this.pos, this.p);
      c.drawText(this.text, pos.x, pos.y, this.p);
    }
  }

  public android.graphics.PointF Calc_Text_Center(
    android.graphics.PointF pos, android.graphics.Paint text_paint)
  {
    float text_height, text_offset;
    android.graphics.PointF res=null;

    text_height = text_paint.descent() - text_paint.ascent();
    text_offset = (text_height / 2f) - text_paint.descent();

    res = new android.graphics.PointF();
    res.x = pos.x;
    res.y = pos.y + text_offset;

    return res;
  }

  public void Draw_Text
  (rs.projecta.view.Game2_View v, 
  String text, float x, float y, float size, android.graphics.Paint p)
  {
    int c;
    float start_pos, new_size;

    new_size = digit_size * size;
    start_pos = -text.length() * new_size / 2f + new_size / 2f + x;
    for (c = 0; c < text.length(); c++)
    {
      this.Draw_Digit(v, text.charAt(c), 
                      start_pos + c * new_size, y, size, p);
    }
  } 
  
  public void Draw_Digit
  (rs.projecta.view.Game2_View v, char text, float x, float y, float size, android.graphics.Paint p)
  {
    int si;

    si=(text-48)*2;

    android.opengl.GLES20.glVertexAttribPointer(
      v.att_loc, 2, android.opengl.GLES20.GL_FLOAT, false, 0, b);
    android.opengl.GLES20.glEnableVertexAttribArray(v.att_loc);

    v.Save_Transform();
    android.opengl.Matrix.translateM(v.proj, 0, x, y, 0);
    android.opengl.Matrix.scaleM(v.proj, 0, size, size, 1f);

    android.opengl.GLES20.glUniformMatrix4fv(v.mat_loc, 1, false, v.proj, 0);
    android.opengl.GLES20.glUniform4f(
      v.col_loc, this.red, this.green, this.blue, this.alpha);
    android.opengl.GLES20.glDrawArrays(
      android.opengl.GLES20.GL_LINES, point_info[si], point_info[si+1]);
      
    v.Restore_Transform();
  }

  public void Init_OpenGL(int col)
  {    
    float[] points;

    points = this.Get_Points();
    b = java.nio.ByteBuffer.allocateDirect(points.length * 4)
      .order(java.nio.ByteOrder.nativeOrder())
      .asFloatBuffer();
    b.put(points);  
    b.position(0);

    this.red = (float)android.graphics.Color.red(col) / 255f;
    this.green = (float)android.graphics.Color.green(col) / 255f;
    this.blue = (float)android.graphics.Color.blue(col) / 255f;
    this.alpha = (float)android.graphics.Color.alpha(col) / 255f;
  }

  public static int[] point_info=
  {
    0, 8, 
    8, 2, 
    10, 10, 
    20, 8, 
    28, 8, 
    36, 10, 
    46, 8, 
    54, 4, 
    58, 10, 
    68, 8
  };
  
  public float[] Get_Points()
  {
    float[] points=
    {
      // 0
      1, 2, -1, 2, 
      -1, 2, -1, -2,
      -1, -2, 1, -2,
      1, -2, 1, 2,
      
      // 1 
      0, 2, 0, -2,

      // 2
      -1f, -2f, 1f, -2f, 
      1f, -2f, 1f, 0, 
      1f, 0, -1f, 0, 
      -1f, 0, -1f, 2f,
      -1f, 2f, 1f, 2f, 

      // 3
      -1f, -2f, 1f, -2f, 
      1f, -2f, 1f, 2f,
      1f, 0, -1f, 0,
      -1f, 2f, 1f, 2f, 

      // 4
      1f, -2f, 1f, 0, 
      -1f, -2f, -1f, 0, 
      1f, 0, -1f, 0, 
      1f, 0, 1f, 2f, 

      // 5
      -1f, -2f, 1f, -2f, 
      -1f, -2f, -1f, 0, 
      1f, 0, -1f, 0, 
      1f, 0, 1f, 2f, 
      -1f, 2f, 1f, 2f, 

      // 6
      -1f, -2f, -1f, 2f, 
      1f, 0, -1f, 0, 
      1f, 0, 1f, 2f,
      -1f, 2f, 1f, 2f, 

      //7
      -1f, -2f, 1f, -2f, 
      1f, -2f, 1f, 2f, 

      // 8
      -1f, -2f, 1f, -2f, 
      1f, -2f, 1f, 2f, 
      -1f, -2f, -1f, 2f, 
      1f, 0, -1f, 0, 
      -1f, 2f, 1f, 2f, 

      // 9
      -1f, -2f, 1f, -2f, 
      1f, -2f, 1f, 2f, 
      -1f, -2f, -1f, 0, 
      1f, 0, -1f, 0, 
    };

    return points;
  }
}
