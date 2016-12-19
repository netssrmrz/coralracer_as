package rs.projecta.view;

public class Game_View
extends android.view.SurfaceView
implements android.view.SurfaceHolder.Callback
{
  android.view.SurfaceHolder surface;
  public World_View world_view;

  public Game_View(android.content.Context ctx, rs.projecta.world.World w)
  {
    super(ctx);

    this.world_view=new World_View(ctx, w);
  }

  @Override
  public void onDraw(android.graphics.Canvas c)
  {
    this.world_view.onDraw(c);
  }

  @Override
  public void surfaceCreated(android.view.SurfaceHolder s)
  {
    //android.util.Log.d("surfaceCreated()", "Entered");
    this.surface = s;
  }

  @Override
  public void surfaceChanged(android.view.SurfaceHolder s, int format, int w, int h)
  {
  }

  @Override
  public void surfaceDestroyed(android.view.SurfaceHolder s)
  {
    //android.util.Log.d("surfaceDestroyed()", "Entered");
    this.surface = null;
  }

  public void Draw_World_Step()
  {
    android.graphics.Canvas c;

    this.surface = this.getHolder();
    if (this.surface != null)
    {
      try {c = this.surface.lockCanvas();}
      catch(java.lang.Exception e) {c=null;}
      if (c != null)
      {
        this.onDraw(c);
        this.getHolder().unlockCanvasAndPost(c);
      }
    }
	}
}
