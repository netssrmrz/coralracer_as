package rs.projecta.level;

public class Editor_Level
extends Level
{
  /*@Override
  public String Get_Next_Level()
  {
    return rs.projecta.level.Tutorial_Turn.class.getName();
  }

  @Override
  public String Get_Title()
  {
    return "Stumble Home";
  }

  @Override
  public String Get_Description()
  {
    return "Big night. Thankfully the way home is straight.";
  }*/

  @Override
  public void Build(rs.projecta.world.World w)
  {
    //this.camera=new rs.projecta.object.Camera(0, 0, null);
    
    w.objs.Add(new rs.projecta.object.Background_Grid());
  }
}
