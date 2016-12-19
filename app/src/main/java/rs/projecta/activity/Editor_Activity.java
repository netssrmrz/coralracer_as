package rs.projecta.activity;

public class Editor_Activity
extends android.app.Activity
implements 
android.widget.Button.OnClickListener,
android.widget.Button.OnLongClickListener,
rs.projecta.dialog.Editor_Objs.OnDialogSelectListener
{
  public static final int BUTTONID_ADD=1;
  public static final int BUTTONID_FWD=10;
  public static final int BUTTONID_RGT=11;
  public static final int BUTTONID_DWN=12;
  public static final int BUTTONID_LFT=13;
  public static final int BUTTONID_DRP=14;
  public static final int BUTTONID_RLF=15;
  public static final int BUTTONID_RRG=16;
  public static final int BUTTONID_SCR=17;

  rs.projecta.world.World world;
  rs.projecta.view.Design_View world_view;
  Object curr_obj, camera;

  @Override
  public void onCreate(android.os.Bundle saved_state)
  {
    android.widget.Button button;
    android.widget.LinearLayout button_bar, main_view;

    super.onCreate(saved_state);

    button_bar = new android.widget.LinearLayout(this);
    button_bar.setOrientation(android.widget.LinearLayout.HORIZONTAL);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setOnLongClickListener(this);
    button.setText("+");
    button.setId(BUTTONID_ADD);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("F");
    button.setId(BUTTONID_FWD);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("D");
    button.setId(BUTTONID_DWN);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText(">");
    button.setId(BUTTONID_RGT);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("<");
    button.setId(BUTTONID_LFT);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("Drop");
    button.setId(BUTTONID_DRP);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("Turn C");
    button.setId(BUTTONID_RRG);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("Turn CC");
    button.setId(BUTTONID_RLF);
    button_bar.addView(button);

    button = new android.widget.Button(this);
    button.setOnClickListener(this);
    button.setText("Script");
    button.setId(BUTTONID_SCR);
    button_bar.addView(button);
    
    world = new rs.projecta.world.World(this, null, new rs.projecta.level.Editor_Level(), true);

    world_view = new rs.projecta.view.Design_View(this, world);

    this.camera = this.world_view.camera;
    this.curr_obj = this.camera;

    main_view = new android.widget.LinearLayout(this);
    main_view.setOrientation(android.widget.LinearLayout.VERTICAL);
    main_view.addView(world_view, 
                      new android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 90f));
    main_view.addView(button_bar, 
                      new android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0, 10f));

    this.setContentView(main_view, 
                        new android.widget.LinearLayout.LayoutParams(
                          android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                          android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1f));
  }

  @Override
  public void onResume()
  {
    super.onResume();

    //android.util.Log.d("Editor_Activity.onResume()", "Entered");
    //this.world_view.Draw_World_Step();
  }

  @Override
  public void onClick(android.view.View button)
  {
    if (button.getId() == BUTTONID_ADD)
    {
      this.curr_obj = new rs.projecta.object.Wall(
        this.world, 
        ((rs.projecta.object.Has_Position)this.camera).Get_X(), 
        ((rs.projecta.object.Has_Position)this.camera).Get_Y(), 
        200, 10, 0);
      this.world.objs.Add(this.curr_obj);
      this.world_view.camera = this.curr_obj;
    }
    else if (button.getId() == BUTTONID_FWD)
    {
      this.Step(0, -10);
    }
    else if (button.getId() == BUTTONID_LFT)
    {
      this.Step(-10, 0);
    }
    else if (button.getId() == BUTTONID_DWN)
    {  
      this.Step(0, 10);
    }
    else if (button.getId() == BUTTONID_RGT)
    {  
      this.Step(10, 0);
    }
    else if (button.getId() == BUTTONID_DRP)
    {
      this.world_view.camera = this.camera;
      this.curr_obj = this.camera;
    }
    else if (button.getId() == BUTTONID_RRG)
    {
      this.Turn(5);
    }
    else if (button.getId() == BUTTONID_RLF)
    {
      this.Turn(-5);
    }
    else if (button.getId() == BUTTONID_SCR)
    {
      rs.android.ui.Util.Show_Text(this, this.world.Gen_Level_Script());
    }

    //this.world.phys_world.step(0.01f, 8, 8);
    this.world_view.invalidate();
  }

  @Override
  public boolean onLongClick(android.view.View button)
  {
    if (button.getId() == BUTTONID_ADD)
      rs.projecta.dialog.Editor_Objs.Show(this, this);
    return false;
  }

  public void onDialogSelect(Object dlg, String selected)
  {
    if (dlg instanceof rs.projecta.dialog.Editor_Objs)
    {
      if (selected.equals("Wall"))
      {
        this.curr_obj = new rs.projecta.object.Wall(
          this.world, 
          ((rs.projecta.object.Has_Position)this.camera).Get_X(), 
          ((rs.projecta.object.Has_Position)this.camera).Get_Y(), 
          200, 10, 0);
        this.world.objs.Add(this.curr_obj);
        this.world_view.camera = this.curr_obj;
      }
      else if (selected.equals("Small Wall"))
      {
        this.curr_obj = new rs.projecta.object.Wall(
          this.world, 
          ((rs.projecta.object.Has_Position)this.camera).Get_X(), 
          ((rs.projecta.object.Has_Position)this.camera).Get_Y(), 
          100, 10, 0);
        this.world.objs.Add(this.curr_obj);
        this.world_view.camera = this.curr_obj;
      }
      else if (selected.equals("Player"))
      {
        this.curr_obj = new rs.projecta.object.Player(
          ((rs.projecta.object.Has_Position)this.camera).Get_X(), 
          ((rs.projecta.object.Has_Position)this.camera).Get_Y(), 
          this.world);
        this.world.objs.Add(this.curr_obj);
        this.world_view.camera = this.curr_obj;
      }
      else if (selected.equals("Goal"))
      {
        this.curr_obj = new rs.projecta.object.Finish(
          this.world,
          ((rs.projecta.object.Has_Position)this.camera).Get_X(), 
          ((rs.projecta.object.Has_Position)this.camera).Get_Y());
        this.world.objs.Add(this.curr_obj);
        this.world_view.camera = this.curr_obj;
      }
      
      //this.world.phys_world.step(0f, 0, 0);
      this.world_view.invalidate();
    }
  }

  public void Step(float x, float y)
  {
    org.jbox2d.common.Vec2 po, pt, pr, pf;
    org.jbox2d.common.Mat22 mr;
    float a=0;

    if (this.curr_obj instanceof rs.projecta.object.Has_Direction)
    {
      a = ((rs.projecta.object.Has_Direction)this.curr_obj).Get_Angle_Degrees();
      a = (float)java.lang.Math.toRadians(a);
    }

    if (this.curr_obj instanceof rs.projecta.object.Has_Position)
    {
      po = new org.jbox2d.common.Vec2(
        ((rs.projecta.object.Has_Position)this.curr_obj).Get_X(),
        ((rs.projecta.object.Has_Position)this.curr_obj).Get_Y());

      pt = new org.jbox2d.common.Vec2(x, y); // forward point
      mr = org.jbox2d.common.Mat22.createRotationalTransform(a);
      pr = mr.mul(pt); // forward point rotated
      pf = new org.jbox2d.common.Vec2(po.x + pr.x, po.y + pr.y);

      ((rs.projecta.object.Has_Position)this.curr_obj).Set_X(pf.x);
      ((rs.projecta.object.Has_Position)this.curr_obj).Set_Y(pf.y);
    }
  }

  public void Turn(float d)
  {
    if (this.curr_obj instanceof rs.projecta.object.Has_Direction)
    {
      ((rs.projecta.object.Has_Direction)this.curr_obj).Set_Angle_Degrees
      (((rs.projecta.object.Has_Direction)this.curr_obj).Get_Angle_Degrees() + d);
    }
  } 
}
