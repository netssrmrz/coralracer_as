package rs.projecta.object;

public class Track_Segment
implements Has_Cleanup, Is_Drawable_OpenGL
{
  public static final float R=400f;
  public static final int TYPE_FORWARD=0;
  public static final int TYPE_TURNRIGHT=1;
  public static final int TYPE_TURNLEFT=-1;

  public static org.jbox2d.common.Vec2[] dir_vecs;

  public Wall w1, w2;
  public rs.projecta.world.World world;
  public org.jbox2d.collision.shapes.PolygonShape bounds;
  public org.jbox2d.common.Vec2 pos;
  public int turn_type;
  public int dir;

  public Track_Segment(rs.projecta.world.World world, org.jbox2d.common.Vec2 pos, int turn_type, int dir)
  {
    this.world = world;
    this.pos = pos;
    this.dir = dir;
    this.turn_type = turn_type;
    if (dir_vecs==null)
    {
      dir_vecs = new org.jbox2d.common.Vec2[4];
      dir_vecs[0] = new org.jbox2d.common.Vec2(0, -R * 2f);
      dir_vecs[1] = new org.jbox2d.common.Vec2(R * 2f, 0);
      dir_vecs[2] = new org.jbox2d.common.Vec2(0, R * 2f);
      dir_vecs[3] = new org.jbox2d.common.Vec2(-R * 2f, 0);
    }

    if (this.turn_type == TYPE_FORWARD)
      New_Forward_Seg();
    else if (this.turn_type == TYPE_TURNRIGHT)
      New_Turn_Right_Seg();
    else if (this.turn_type == TYPE_TURNLEFT)
      New_Turn_Left_Seg();

    bounds=new org.jbox2d.collision.shapes.PolygonShape();
    bounds.setAsBox(R, R, pos, 0);
  }

  public void New_Forward_Seg()
  {
    org.jbox2d.common.Vec2 w_pos;

    if (dir==0 || dir==2)
    {
      // left wall
      w_pos = new org.jbox2d.common.Vec2(pos.x - R, pos.y);
      this.w1 = new Wall(world, w_pos, 20f, R, 0);
      // right wall
      w_pos.set(pos.x + R, pos.y);
      this.w2 = new Wall(world, w_pos, 20f, R, 0);
    }
    else
    {
      // top wall
      w_pos = new org.jbox2d.common.Vec2(pos.x, pos.y + R);
      this.w1 = new Wall(world, w_pos, R, 20f, 0);
      // bottom wall
      w_pos.set(pos.x, pos.y - R);
      this.w2 = new Wall(world, w_pos, R, 20f, 0);
    }
  }

  public void New_Turn_Right_Seg()
  {
    org.jbox2d.common.Vec2 w_pos;

    if (dir==0 || dir==2)
    {
      w_pos = new org.jbox2d.common.Vec2(pos.x, pos.y);
      this.w1 = new Wall(world, w_pos, 20f, R*1.5f, 45);
    }
    else
    {
      w_pos = new org.jbox2d.common.Vec2(pos.x, pos.y);
      this.w1 = new Wall(world, w_pos, 20f, R*1.5f, 135);
    }
  }

  public void New_Turn_Left_Seg()
  {
    org.jbox2d.common.Vec2 w_pos;

    if (dir==0 || dir==2)
    {
      w_pos = new org.jbox2d.common.Vec2(pos.x, pos.y);
      this.w1 = new Wall(world, w_pos, 20f, R*1.5f, 135);
    }
    else
    {
      w_pos = new org.jbox2d.common.Vec2(pos.x, pos.y);
      this.w1 = new Wall(world, w_pos, 20f, R*1.5f, 45);
    }
  }

  @Override
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    if (this.w1!=null)
      v.Draw_Obj(this.w1);
    if (this.w2!=null)
      v.Draw_Obj(this.w2);
  }

  public void Remove()
  {
    if (this.w1!=null)
      this.w1.Remove();
    if (this.w2!=null)
      this.w2.Remove();
  }

  public int Get_Next_Dir()
  {
    int dir=0;

    dir = this.dir+this.turn_type;
    if (dir>3)
      dir=0;
    else if (dir<0)
      dir=3;

    return dir;
  }

  public org.jbox2d.common.Vec2 Get_Next_Pos(int dir)
  {
    org.jbox2d.common.Vec2 res=null;

    res=this.pos.add(dir_vecs[dir]);

    return res;
  }
}
