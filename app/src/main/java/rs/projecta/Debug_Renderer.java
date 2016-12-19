package rs.projecta;
import org.jbox2d.common.*;

public class Debug_Renderer
extends org.jbox2d.callbacks.DebugDraw
{
  public android.graphics.Canvas canvas;
  public android.graphics.Paint col, col_fill, col_txt;
  public android.graphics.Path path;
  public float phys_scale;
  
  public Debug_Renderer(float phys_scale)
  {
    super(new org.jbox2d.common.OBBViewportTransform());
    
    this.setFlags(
      org.jbox2d.callbacks.DebugDraw.e_shapeBit | 
      //org.jbox2d.callbacks.DebugDraw.e_aabbBit | 
      org.jbox2d.callbacks.DebugDraw.e_pairBit | 
      org.jbox2d.callbacks.DebugDraw.e_centerOfMassBit | 
      org.jbox2d.callbacks.DebugDraw.e_jointBit 
      //org.jbox2d.callbacks.DebugDraw.e_dynamicTreeBit
      );
      
    this.col_fill = new android.graphics.Paint();
    this.col_fill.setStyle(android.graphics.Paint.Style.FILL);
    this.col_fill.setColor(0x44ffffff);
    
    this.col = new android.graphics.Paint();
    this.col.setStyle(android.graphics.Paint.Style.STROKE);
    this.col.setColor(0x44ffffff);
    
    this.col_txt = new android.graphics.Paint();
    this.col_txt.setStyle(android.graphics.Paint.Style.FILL);
    this.col_txt.setColor(0xffffffff);
    this.col_txt.setTextSize(20);
    
    this.path = new android.graphics.Path();
    
    this.phys_scale=phys_scale;
    this.setCamera(0, 0, this.phys_scale);
  }

  @Override
  public void drawPoint(Vec2 p, float r, Color3f col)
  {
    //this.col.setARGB(1, (int)col.x, (int)col.y, (int)col.z);
    p=this.getWorldToScreen(p);
    this.canvas.drawCircle(p.x, p.y, r*this.phys_scale, this.col_fill);
  }

  @Override
  public void drawSolidPolygon(Vec2[] v, int v_count, Color3f col)
  {
    int c;
    
    this.path.reset();
    for (c=0; c<v_count; c++)
    {
      v[c]=this.getWorldToScreen(v[c]);
      if (c==0)
        this.path.moveTo(v[c].x, v[c].y);
      else
        this.path.lineTo(v[c].x, v[c].y);
    }

    //this.col.setARGB(1, (int)col.x, (int)col.y, (int)col.z);
    canvas.drawPath(this.path, this.col_fill);
  }

  @Override
  public void drawCircle(Vec2 p, float r, Color3f color)
  {
    p=this.getWorldToScreen(p);
    this.canvas.drawCircle(p.x, p.y, r*this.phys_scale, this.col);
  }

  @Override
  public void drawSolidCircle(Vec2 p, float r, Vec2 axis, Color3f color)
  {
    p=this.getWorldToScreen(p);
    this.canvas.drawCircle(p.x, p.y, r*this.phys_scale, this.col_fill);
  }

  @Override
  public void drawSegment(Vec2 p1, Vec2 p2, Color3f col)
  {
    //this.col.setARGB(1, (int)col.x, (int)col.y, (int)col.z);
    p1=this.getWorldToScreen(p1);
    p2=this.getWorldToScreen(p2);
    this.canvas.drawLine(p1.x, p1.y, p2.x, p2.y, this.col);
  }

  @Override
  public void drawTransform(Transform xf)
  {
    //android.util.Log.i(TAG, "Draw transform");
  }

  @Override
  public void drawString(float x, float y, String s, Color3f color)
  {
    this.canvas.drawText(s, x, y, this.col_txt);
  }
}
