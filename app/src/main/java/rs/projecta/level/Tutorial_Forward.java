package rs.projecta.level;

public class Tutorial_Forward
extends Level
{
  public rs.projecta.object.Player player;
  public rs.projecta.world.World w;
  
  @Override
  public String Get_Next_Level()
  {
    return rs.projecta.level.Tutorial_Turn.class.getName();
  }
  
  @Override
  public String Get_Title()
  {
    return "Learn to Steer";
  }
  
  @Override
  public String Get_Description()
  {
    return "Tilt the device left and right to steer to the blue circle.";
  }
  
  @Override
  public void Build(rs.projecta.world.World w)
  {
    rs.projecta.object.Finish finish;

    player = new rs.projecta.object.Player(0, -20, w);
    finish = new rs.projecta.object.Finish(w, 0, -3800);
    this.w=w;

    w.objs.Add(new rs.projecta.object.Background_Grid());
    w.objs.Add(finish);
    w.objs.Add(player);

    this.Add_Start_Walls(0);
    this.Add_Walls(1000);
  }
  
  public void Add_Walls(float trg_pos)
  {
    this.w.objs.Add(new rs.projecta.object.Wall(
      this.w, 500, trg_pos, 20, 500, 0f));
    this.w.objs.Add(new rs.projecta.object.Wall(
      this.w, -500, trg_pos, 20, 500, 0f));
  }
  
  public void Add_Start_Walls(float trg_pos)
  {
    this.w.objs.Add(new rs.projecta.object.Wall(
      this.w, 500, -500, 20, 500, 0f));
    this.w.objs.Add(new rs.projecta.object.Wall(
      this.w, -500, -500, 20, 500, 0f));
    this.w.objs.Add(new rs.projecta.object.Wall(
      this.w, 00, trg_pos, 500, 20, 0f));
  }
  
  @Override
  public void Update()
  {
    
  }
}
