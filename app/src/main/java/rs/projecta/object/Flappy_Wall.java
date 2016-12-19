package rs.projecta.object;
import rs.projecta.view.*;

public class Flappy_Wall
implements 
  Is_Drawable, Is_Drawable_OpenGL, 
  Has_Position, Has_Direction, Has_Cleanup
{
  public float x, y, a;
  public Wall w1, w2, door;
  public float tot_width, gap_width;
  public float w1x, w2_x, door_x;
  public boolean door_closed;
  public rs.projecta.world.World world;

  public Flappy_Wall(rs.projecta.world.World world, float x, float y, 
                     float a_degrees, float gap_loc)
  {
    float wall_width, gap_pos;
    android.graphics.PointF p;

    p = new android.graphics.PointF();
    this.world=world;
    this.door_closed=false;
    this.x = x;
    this.y = y;
    this.a = a_degrees;
    this.tot_width = 1000;
    this.gap_width = 100;
    gap_pos = this.tot_width * gap_loc;
    if (gap_pos<this.gap_width/2f+20)
      gap_pos=this.gap_width/2f+20;
    if (gap_pos>this.tot_width-this.gap_width/2f-20)
      gap_pos=this.tot_width-this.gap_width/2f-20;

    // left wall
    wall_width = gap_pos - gap_width / 2f;
    if (wall_width > 20)
    {
      this.w1x = (wall_width - tot_width) / 2f;
      p = rs.android.ui.Util.Rotate(x, y, this.w1x, 0, a);
      this.w1 = new Wall(world, p.x, p.y, wall_width / 2f, 40, a);
    }

    // right wall
    wall_width = tot_width - gap_pos - gap_width / 2f;
    if (wall_width > 20)
    {
      this.w2_x = (tot_width - wall_width) / 2f;
      p = rs.android.ui.Util.Rotate(x, y, this.w2_x, 0, a);
      this.w2 = new Wall(world, p.x, p.y, wall_width / 2f, 40, a);
    }
    
    // door
    wall_width = gap_width;
    this.door_x = gap_pos-tot_width/2f;
    p = rs.android.ui.Util.Rotate(x, y, this.door_x, 0, a);
    this.door = new Wall(world, p.x, p.y, wall_width / 2f, 40, a);
    this.Open();
  }

  @Override
  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    if (this.w1 != null)
    {
      c.save();
      c.translate(w1x, 0);
      this.w1.Draw(v, c);
      c.restore();
    }

    if (this.w2 != null)
    {
      c.save();
      c.translate(w2_x, 0);
      this.w2.Draw(v, c);
      c.restore();
    }
    
    if (this.door != null && this.door_closed)
    {
      c.save();
      c.translate(door_x, 0);
      this.door.Draw(v, c);
      c.restore();
    }
  }

  public void Draw_OpenGL(Game2_View v)
  {
    if (this.w1 != null)
    {
      v.Save_Transform();
      android.opengl.Matrix.translateM(v.proj, 0, w1x, 0, 0);
      this.w1.Draw_OpenGL(v);
      v.Restore_Transform();
    }

    if (this.w2 != null)
    {
      v.Save_Transform();
      android.opengl.Matrix.translateM(v.proj, 0, w2_x, 0, 0);
      this.w2.Draw_OpenGL(v);
      v.Restore_Transform();
    }

    if (this.door != null && this.door_closed)
    {
      v.Save_Transform();
      android.opengl.Matrix.translateM(v.proj, 0, door_x, 0, 0);
      this.door.Draw_OpenGL(v);
      v.Restore_Transform();
    }
  }

  public float Get_X()
  {
    return this.x;
  }

  public float Get_Y()
  {
    return this.y;
  }

  public void Set_X(float x)
  {
    /*org.jbox2d.common.Vec2 curr_pos, new_pos;
     float angle;

     curr_pos=this.body.getPosition();
     new_pos=new org.jbox2d.common.Vec2(x/world.phys_scale, curr_pos.y);
     angle=this.body.getAngle();

     this.body.setTransform(new_pos, angle);*/
  }

  public void Set_Y(float y)
  {
    /*org.jbox2d.common.Vec2 curr_pos, new_pos;
     float angle;

     curr_pos=this.body.getPosition();
     new_pos=new org.jbox2d.common.Vec2(curr_pos.x, y/world.phys_scale);
     angle=this.body.getAngle();

     this.body.setTransform(new_pos, angle);*/
  }

  public float Get_Angle_Degrees()
  {
    return this.a;
  }

  public void Set_Angle_Degrees(float a)
  {
    //this.body.setTransform(this.body.getPosition(), 
    //                       (float)java.lang.Math.toRadians(a));
  }

  public void Remove()
  {
    if (this.w1!=null)
      this.w1.Remove();
    if (this.w2!=null)
      this.w2.Remove();
    this.door.Remove();
  }
  
  public void Open()
  {
    org.jbox2d.dynamics.Filter filter;

    filter=this.door.body.getFixtureList().getFilterData();
    filter.groupIndex=-1;
    this.door.body.getFixtureList().setFilterData(filter);
    this.door_closed=false;
  }
  
  public void Close()
  {
    org.jbox2d.dynamics.Filter filter;
    
    //android.util.Log.d("Close", "Entry");
    filter=this.door.body.getFixtureList().getFilterData();
    filter.groupIndex=0;
    this.door.body.getFixtureList().setFilterData(filter);
    this.door_closed=true;
  }
}
