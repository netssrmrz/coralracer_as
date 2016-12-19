package rs.projecta.level;

public class Level
{
  //public rs.projecta.object.Player player;
  //public Object camera;
  
  public String Get_Next_Level()
  {
    return null;
  }
  
  public String Get_Title()
  {
    return null;
  }
  
  public void Build(rs.projecta.world.World w)
  {
    
  }
  
  public String Get_Description()
  {
    return null;
  }
  
  public static rs.projecta.level.Level Get(android.app.Activity a)
  {
    String class_name;
    rs.projecta.level.Level res=null;

    class_name=a.getIntent().getStringExtra("level_class");
    if (rs.android.Util.NotEmpty(class_name))
      res=Level.Get(class_name);

    return res;
  }
  
  public static rs.projecta.level.Level Get(String class_name)
  {
    Class<? extends Level> level_class=null;
    rs.projecta.level.Level res=null;

    try 
    {
      level_class=(Class<? extends Level>)Class.forName(class_name);
    }
    catch (Exception e) 
    {
      level_class=null;
    }

    if (level_class!=null)
      res=Level.Get(level_class);

    return res;
  }
  
  public static rs.projecta.level.Level Get(Class<? extends Level> level_class)
  {
    rs.projecta.level.Level res=null;

    try 
    {
      res = level_class.newInstance();
    }
    catch (Exception e) 
    {
      res=null;
    }

    return res;
  }
  
  public void Update()
  {
    
  }
  
  /*public float Get_Camera_X()
  {
    float res=0;
    
    if (this.camera!=null && this.camera instanceof rs.projecta.object.Has_Position)
    {
      res=((rs.projecta.object.Has_Position)this.camera).Get_X();
    }
    return res;
  }
  
  public float Get_Camera_Y()
  {
    float res=0;

    if (this.camera!=null && this.camera instanceof rs.projecta.object.Has_Position)
    {
      res=((rs.projecta.object.Has_Position)this.camera).Get_Y();
    }
    return res;
  }*/
}
