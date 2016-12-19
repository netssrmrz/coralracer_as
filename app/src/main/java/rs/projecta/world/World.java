package rs.projecta.world;
//import android.media.*;

public class World
implements 
org.jbox2d.callbacks.ContactListener,
android.media.SoundPool.OnLoadCompleteListener,
java.lang.Runnable
{
  public org.jbox2d.dynamics.World phys_world;
  public float phys_scale;
  public String debug_msg[];
  public boolean debug, prof, is_fast;
  public rs.projecta.level.Level level;
  public Thread game_loop;
  public rs.projecta.world.World_Step_Listener world_step_listener;
  public rs.projecta.world.Object_List objs;
  public int state;
  public boolean do_processing;
  public java.util.Random rnd;
  public long last_update, lapsed_time;
  public android.media.SoundPool sounds;
  public int soundid_whack, soundid_start, soundid_door;
  public android.content.Context ctx;

  public static final int STATE_STOP=0;
  public static final int STATE_PLAY=1;
  public static final int STATE_LEVELCOMPLETE=2;
  public static final int STATE_QUIT=3;
  public static final int STATE_LEVELFAIL=4;

  public World(
    android.content.Context ctx,
    rs.projecta.world.World_Step_Listener l, 
    rs.projecta.level.Level level,
    boolean is_fast)
  {
    //this.debug=true;
    //this.prof=true;
    this.is_fast = is_fast;
    this.ctx = ctx;
    this.debug_msg = new String[5];
    this.world_step_listener = l;

    this.Init_Sound();
    this.Init_Level(level);
  }

  public void Init_Sound()
  {
    
  }
  
  public void xInit_Sound()
  {
    android.media.SoundPool.Builder b;
    android.media.AudioAttributes.Builder a;

    if (this.sounds == null)
    {
      if (android.os.Build.VERSION.SDK_INT >= 
          android.os.Build.VERSION_CODES.LOLLIPOP)
      {
        a = new android.media.AudioAttributes.Builder();
        a.setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION);
        a.setUsage(android.media.AudioAttributes.USAGE_GAME);

        b = new android.media.SoundPool.Builder();
        b.setAudioAttributes(a.build());
        b.setMaxStreams(3);
        this.sounds = b.build();
      }
      else
      {
        this.sounds = new android.media.SoundPool
          (1, android.media.AudioManager.STREAM_MUSIC, 0);
      }

      this.soundid_whack = this.sounds.load(ctx, rs.projecta.R.raw.whack, 1);
      this.soundid_start = this.sounds.load(ctx, rs.projecta.R.raw.start, 1);
      this.soundid_door = this.sounds.load(ctx, rs.projecta.R.raw.door, 1);
    }
  }

  public void beginContact(org.jbox2d.dynamics.contacts.Contact c)
  {
    Object obj;

    obj = c.getFixtureA().getBody().getUserData();
    if (obj != null && obj instanceof rs.projecta.object.Can_Collide)
      ((rs.projecta.object.Can_Collide)obj).Contact(c);

    obj = c.getFixtureB().getBody().getUserData();
    if (obj != null && obj instanceof rs.projecta.object.Can_Collide)
      ((rs.projecta.object.Can_Collide)obj).Contact(c);
  }

  public void endContact(org.jbox2d.dynamics.contacts.Contact p1)
  {
    // TODO: Implement this method
  }

  public void preSolve(org.jbox2d.dynamics.contacts.Contact p1, 
                       org.jbox2d.collision.Manifold p2)
  {
    // TODO: Implement this method
  }

  public void postSolve(org.jbox2d.dynamics.contacts.Contact p1, 
                        org.jbox2d.callbacks.ContactImpulse p2)
  {
    // TODO: Implement this method
  }

  public void Init_Level(rs.projecta.level.Level level)
  {
    this.level = level;
    this.Init_Level();
  }

  public void Init_Level()
  {
    //android.util.Log.d("Init_Level()", "Entered");

    this.rnd = new java.util.Random(0);
    this.last_update = System.nanoTime();
    this.state = STATE_PLAY; 
    this.phys_world = new org.jbox2d.dynamics.World(new org.jbox2d.common.Vec2(0, 0));
    this.phys_world.setAllowSleep(true);
    this.phys_world.setContactListener(this);

    this.phys_scale = 20f;
    this.objs = new rs.projecta.world.Object_List(this);

    if (this.level != null)
      this.level.Build(this);

    if (this.world_step_listener != null)
      this.world_step_listener.On_World_Init(this);
  }
  
  public void Signal_End(int state)
  {
    this.state = state;
    if (this.game_loop!=null)
      this.do_processing = false;
  }
  
  public void End(int state)
  {
    this.state=state;
  }
  
  public void run()
  {
    //android.util.Log.d("World.run()", "Entered");

    if (this.prof)
      android.os.Debug.startMethodTracing("rs.projecta");

    this.do_processing = true;
    while (this.do_processing)
      this.Update();

    if (this.prof)
      android.os.Debug.stopMethodTracing();

    this.game_loop = null;
    
    if (this.world_step_listener != null)
      this.world_step_listener.On_World_Finish(this);
  }
  
  public void Update()
  {
    long now;
    float sec_step;
    
    now = System.nanoTime();
    this.lapsed_time = now - this.last_update;
    this.last_update = now;

    sec_step = this.lapsed_time / 1800000000f;
    this.phys_world.step(sec_step, 8, 8);

    if (this.world_step_listener != null)
      this.world_step_listener.On_World_Step(this); // draw

    if (this.level != null)
      this.level.Update();

    this.objs.Process(); // remove and update
    
    if (this.game_loop==null && this.world_step_listener != null &&
      this.state!=STATE_PLAY)
      this.world_step_listener.On_World_Finish(this);
  }

  public void onResume(boolean with_thread)
  {
    //android.util.Log.d("Start_Loop()", "Entered");
    if (with_thread && this.game_loop == null)
    {
      this.game_loop = new Thread(this);
      this.game_loop.start();
    }
    
    this.Init_Sound();
  }

  public void onPause()
  {
    //android.util.Log.d("Stop_Loop()", "Entered");
    if (this.game_loop != null)
    {
      this.do_processing = false;
      try
      {this.game_loop.join();}
      catch (java.lang.Exception e) {}
    } 
    
    if (this.sounds!=null)
    {
      this.sounds.release();
      this.sounds = null;
    }
  }

  public String Gen_Level_Script()
  {
    String s;

    s =
      "package rs.projecta.level;\n\n" +
      "public class ???\n" +
      "extends Level\n" +
      "{\n" +
      "  @Override\n" +
      "  public String Get_Next_Level()\n" +
      "  {\n" +
      "    return rs.projecta.level.???.class.getName();\n" +
      "  }\n\n" +
      "  @Override\n" +
      "  public String Get_Title()\n" +
      "  {\n" +
      "    return \"???\";\n" +
      "  }\n\n" +
      "  @Override\n" +
      "  public String Get_Description()\n" +
      "  {\n" +
      "    return \"???\";\n" +
      "  }\n\n" +
      "  @Override\n" +
      "  public void Build(rs.projecta.World w)\n" +
      "  {\n";

    for (Object obj: this.objs.objs)
    {
      if (obj instanceof rs.projecta.object.Finish)
      {
        s += "    w.objs.add(new rs.projecta.object.Finish(w, " +
          ((rs.projecta.object.Finish)obj).Get_X() + ", " +
          ((rs.projecta.object.Finish)obj).Get_Y() + "));\n";
      }
    }
    /*w.objs.add(new rs.projecta.object.Finish(w, 0, -3800));
     w.objs.add(new rs.projecta.object.Wall(w, 0, -3900, 180, 10, 0)); // top
     w.objs.add(new rs.projecta.object.Wall(w, 200, -1900, 10, 2000, 0)); // right
     w.objs.add(new rs.projecta.object.Wall(w, -200, -1900, 10, 2000, 0)); // left
     w.objs.add(new rs.projecta.object.Wall(w, 0, 100, 180, 10, 0)); // bottom
     w.objs.add(new rs.projecta.object.Player(0, 0, w));
     w.objs.add(new rs.projecta.object.Background());*/
    s = s +
      "  }\n" +
      "}\n";
    return s;
  }

  public void onLoadComplete(android.media.SoundPool sounds, int id, int status)
  {
    if (status == 0)
      android.util.Log.d("World.onLoadComplete()", "sound load complete");
    else
      android.util.Log.d("World.onLoadComplete()", "sound load failed");
  }

  public org.jbox2d.common.Vec2 To_Phys_Pt(org.jbox2d.common.Vec2 pt)
  {
    pt.x = To_Phys_Dim(pt.x);
    pt.y = To_Phys_Dim(pt.y);
    return pt;
  }

  public float To_Phys_Dim(float d)
  {
    return d / this.phys_scale;
  }
}
