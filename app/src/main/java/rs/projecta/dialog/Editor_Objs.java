package rs.projecta.dialog;

public class Editor_Objs
implements android.content.DialogInterface.OnClickListener
{
  public interface OnDialogSelectListener
  {
    public void onDialogSelect(Object dlg, String selected);
  }
  
  public Class<? extends Object> selected_obj;
  OnDialogSelectListener dlg_sel_listener;
  public String[] types=
  {
    "Wall",
    "Small Wall",
    "Player",
    "Goal"
  };
  
  public static void Show(android.content.Context ctx, 
    OnDialogSelectListener listener)
  {
    Editor_Objs dlg;
    android.app.AlertDialog.Builder build;
    
    
    dlg=new Editor_Objs();
    dlg.dlg_sel_listener=listener;
    
    build=new android.app.AlertDialog.Builder(ctx);
    //build.setPositiveButton("OK", dlg);
    build.setNegativeButton("Cancel", dlg);
    build.setItems(dlg.types, dlg);
    build.setTitle("Select a game object.");
    build.create().show();
  }
  
  public void onClick(android.content.DialogInterface i, int selected)
  {
    if (selected!=android.content.DialogInterface.BUTTON_NEGATIVE)
    {
      this.dlg_sel_listener.onDialogSelect(this, this.types[selected]);
    }
  }
}
