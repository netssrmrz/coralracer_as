package rs.projecta.activity;

public class Main_Activity 
extends android.app.Activity
implements android.widget.Button.OnClickListener
{ 
  public android.widget.LinearLayout list_view;
  
	@Override
	public void onCreate(android.os.Bundle saved_state)
	{
    android.widget.TextView text_view;
		android.widget.Button button;
    rs.projecta.level.Level level=null;
    android.widget.LinearLayout.LayoutParams layout;
    
		super.onCreate(saved_state);
    this.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    //level = rs.projecta.level.Level.Get(rs.projecta.level.Flappy_Bird.class);
    level = rs.projecta.level.Level.Get(rs.projecta.level.Race_Level.class);

    list_view = new android.widget.LinearLayout(this);
    list_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    list_view.setBackground(new rs.projecta.view.Wave_Drawable());
    new rs.android.ui.Animate(list_view);
    
    text_view=new android.widget.TextView(this);
    text_view.setTextColor(0xff009900);
    text_view.setTextSize(18);
    text_view.setText(level.Get_Description());
    text_view.setGravity(
      android.view.Gravity.BOTTOM|android.view.Gravity.CENTER_HORIZONTAL);
    list_view.addView(text_view,
      new android.widget.LinearLayout.LayoutParams(
        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 90f)
    );
    
    layout=new android.widget.LinearLayout.LayoutParams(
      android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 10f);
    layout.setMargins(50, 50, 50, 50);
    button = new android.widget.Button(this);
    button.setTag(level.getClass().getName());
    button.setOnClickListener(this);
    button.setText("Start!");
    button.setId(1);
    list_view.addView(button, layout);
    
    this.setContentView(list_view,
      new android.widget.LinearLayout.LayoutParams(
        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1f)
      );
	}

  @Override
  public void onClick(android.view.View button)
  {
    android.content.Intent i;
    String level_class;
    rs.projecta.view.Wave_Drawable b;
    boolean is_fast=false;
    
    b=(rs.projecta.view.Wave_Drawable)this.list_view.getBackground();
    //rs.android.ui.Util.Show_Note(this, "min_elapsed: "+b.min_elapsed);
    if (b.min_elapsed<10000000)
      is_fast=true;

    level_class = (String)button.getTag();
    i = new android.content.Intent(this, Play2_Activity.class);
    //i = new android.content.Intent(this, Play_Activity.class);
    i.setFlags(android.content.Intent.FLAG_ACTIVITY_NO_HISTORY);
    i.putExtra("level_class", level_class);
    i.putExtra("is_fast", is_fast);
    this.startActivity(i);
  }
}
