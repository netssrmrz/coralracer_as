package rs.projecta.object;

public class Camera
implements Has_Position
{
  //public rs.projecta.World world;
  public float x, y;

  public Camera(float x, float y) //, rs.projecta.World world)
  {
    //this.world=world;
    this.x=x;
    this.y=y;
  }

  public float Get_X()
  {
    return x;
  }

  public float Get_Y()
  {
    return y;
  }

  public void Set_X(float x)
  {
    this.x=x;
  }

  public void Set_Y(float y)
  {
    this.y=y;
  }
}
