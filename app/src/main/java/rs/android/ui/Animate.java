package rs.android.ui;

public class Animate
implements java.lang.Runnable
{
  android.view.View view;
  android.os.Handler handler;

  public Animate(android.view.View v)
  {
    this.view=v;
    this.handler=new android.os.Handler();
    this.handler.post(this);
  }

  public void run ()
  {
    this.view.invalidate();
    this.handler.post(this);
  }
}
