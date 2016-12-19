package rs.projecta.activity;

public class Finish_Activity
extends android.app.Activity
implements 
android.widget.Button.OnClickListener
{
  public rs.projecta.level.Level curr_level;
  
  @Override
  public void onCreate(android.os.Bundle saved_state)
  {
    android.widget.Button button;
    android.widget.TextView msg_view;
    android.widget.LinearLayout layout_view;
    
    super.onCreate(saved_state);

    this.curr_level=rs.projecta.level.Level.Get(this);
    
    msg_view=new android.widget.TextView(this);
    msg_view.setText("Well Done!");
    msg_view.setTextSize(10f);
    
    button=new android.widget.Button(this);
    button.setText("Start Next Level");
    button.setOnClickListener(this);
    
    layout_view=new android.widget.LinearLayout(this);
    layout_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    layout_view.addView(msg_view);
    layout_view.addView(button);

    this.setContentView(layout_view);
  }

  @Override
  public void onClick(android.view.View v)
  {
    android.content.Intent i;

    i=new android.content.Intent(this, Play_Activity.class);
    i.setFlags(android.content.Intent.FLAG_ACTIVITY_NO_HISTORY);
    i.putExtra("level_class", this.curr_level.Get_Next_Level());
    this.startActivity(i);
  }
}
