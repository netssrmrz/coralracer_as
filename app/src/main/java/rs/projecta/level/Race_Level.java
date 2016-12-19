package rs.projecta.level;

import rs.projecta.object.Track_Segment;

public class Race_Level
extends Level
{
  public rs.projecta.object.Player player;
  public int trg_seg;
  public rs.projecta.world.World w;
  public int score;

  @Override
  public String Get_Next_Level()
  {
    return null;
  }

  @Override
  public String Get_Title()
  {
    return "Click to Start";
  }

  @Override
  public String Get_Description()
  {
    return "Tilt this device left and right to steer Mr. Fish through "+
      "the coral walls.";
  }

  @Override
  public void Build(rs.projecta.world.World w)
  {
    rs.projecta.object.Background_Waves bd, bd2, bd3;

    player = new rs.projecta.object.Player(0, -100, w);
    bd=new rs.projecta.object.Background_Waves(this.player, 1.2f, 0xff0000ff);
    bd2=new rs.projecta.object.Background_Waves(this.player, 1.4f, 0xff0000cc);
    bd3=new rs.projecta.object.Background_Waves(this.player, 1.6f, 0xff000088);
    
    this.trg_seg = 1;
    this.w = w;
    this.score = 0;

    this.w.objs.Add(bd3);
    this.w.objs.Add(bd2);
    this.w.objs.Add(bd);
    this.w.objs.Add(player);
    this.Add_Segment(Track_Segment.TYPE_FORWARD);
    this.Add_Segment(Track_Segment.TYPE_FORWARD);
    this.Add_Segment(Track_Segment.TYPE_TURNRIGHT);
    this.Add_Segment(Track_Segment.TYPE_FORWARD);
    this.Add_Segment(Track_Segment.TYPE_TURNLEFT);
  }

  @Override
  public void Update()
  {
    if (this.Get_Curr_Seg()>this.trg_seg)
    {
      this.Add_Score();
      this.Extend_Track();
      //this.trg_seg++;

      if (this.w.objs.Get_Count(rs.projecta.object.Track_Segment.class) > 3)
      {
        this.Remove_Segment();
        //this.Close_Door();
      }
      
      //if (this.player.Get_Y() < -this.trg_step)
        //this.w.sounds.play(this.w.soundid_door, 1, 1, 0, 0, 1);
    }
  }

  public void Extend_Track()
  {
    this.Add_Segment(Track_Segment.TYPE_FORWARD);
  }

  public int Get_Curr_Seg()
  {
    int res=0, i=0;
    org.jbox2d.common.Vec2 player_pos;
    org.jbox2d.common.Transform t;

    t=new org.jbox2d.common.Transform();
    player_pos=new org.jbox2d.common.Vec2(this.player.Get_X(), this.player.Get_Y());
    for (Object o: this.w.objs.objs)
    {
      if (o instanceof rs.projecta.object.Track_Segment)
      {
        if (((Track_Segment)o).bounds.testPoint(t, player_pos))
        {
          res=i;
          break;
        }
        i++;
      }
    }
    return res;
  }

  public void Add_Segment(int turn_type)
  {
    rs.projecta.object.Track_Segment first_seg;
    org.jbox2d.common.Vec2 pos;
    int dir;

    first_seg = (rs.projecta.object.Track_Segment)this.w.objs.Get_One(rs.projecta.object.Track_Segment.class, true);
    if (first_seg!=null)
    {
      dir = first_seg.Get_Next_Dir();
      pos = first_seg.Get_Next_Pos(dir);
    }
    else
    {
      dir = 0;
      pos = new org.jbox2d.common.Vec2();
    }

    this.w.objs.Add(new rs.projecta.object.Track_Segment(w, pos, turn_type, dir));
  }

  public void Remove_Segment()
  {
    Object last_seg;

    last_seg = this.w.objs.Get_One(rs.projecta.object.Track_Segment.class, false);
    this.w.objs.Remove(last_seg);
  }

  /*public void Close_Door()
  {
    rs.projecta.object.Flappy_Wall wall;

    wall = (rs.projecta.object.Flappy_Wall)
      this.w.objs.Get_One(rs.projecta.object.Flappy_Wall.class, false);
    wall.Close();
  }*/

  public void Add_Score()
  {
    rs.projecta.object.Score score;

    this.score++;
    if (this.score > 0)
    {
      score = new rs.projecta.object.Score(this.w, Integer.toString(this.score), 
        this.player);
      this.w.objs.Add(score);
    }
  }
}
