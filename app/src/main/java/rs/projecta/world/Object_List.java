package rs.projecta.world;

public class Object_List
{
  public java.util.ArrayList<Object> objs, del_objs;
  public World world;

  public Object_List(World w)
  {
    this.world = w;
    this.objs = new java.util.ArrayList<Object>();
    this.del_objs = new java.util.ArrayList<Object>();
  }

  public void Process()
  {
    for (Object obj: this.objs)
    {
      if (obj instanceof rs.projecta.object.Has_Auto_Movement)
        ((rs.projecta.object.Has_Auto_Movement)obj).Update(1);
    }

    for (Object obj: this.del_objs)
    {
      this.objs.remove(obj);
      if (obj instanceof rs.projecta.object.Has_Cleanup)
        ((rs.projecta.object.Has_Cleanup)obj).Remove();
    }
    this.del_objs.clear();
  }

  public void Remove(Object obj)
  {
    this.del_objs.add(obj);
  }

  public void Add(Object obj)
  {
    this.objs.add(obj);
  }

  public Object Get_One(Class obj_class, boolean get_newest)
  {
    Object res=null;

    for (Object obj: this.objs)
    {
      if (obj.getClass() == obj_class && !this.del_objs.contains(obj))
      {
        res = obj;
        if (!get_newest)
          break;
      }
    }

    return res;
  }

  public rs.projecta.object.Player Get_Player()
  {
    return (rs.projecta.object.Player)this.Get_One(
      rs.projecta.object.Player.class, false);
  }

  public int Get_Count(Class<? extends Object> obj_class)
  {
    int res=0;

    for (Object o: this.objs)
    {
      if (o.getClass() == obj_class)
      {
        res++;
      }
    }
    return res;
  }

  public void Draw(rs.projecta.view.World_View v, android.graphics.Canvas c)
  {
    for (Object o: this.objs)
    {
      if (o instanceof rs.projecta.object.Is_Drawable)
      {
        c.save();

        if (o instanceof rs.projecta.object.Has_Position)
          c.translate(
            ((rs.projecta.object.Has_Position)o).Get_X(), 
            ((rs.projecta.object.Has_Position)o).Get_Y());

        if (o instanceof rs.projecta.object.Has_Direction)
          c.rotate(((rs.projecta.object.Has_Direction)o).Get_Angle_Degrees());

        ((rs.projecta.object.Is_Drawable)o).Draw(v, c);

        c.restore();
      }
    }
  }
  
  public void Draw_OpenGL(rs.projecta.view.Game2_View v)
  {
    for (Object o: this.objs)
    {
      if (o instanceof rs.projecta.object.Is_Drawable_OpenGL)
      {
        v.Draw_Obj((rs.projecta.object.Is_Drawable_OpenGL)o);
      }
    }
  }
}
