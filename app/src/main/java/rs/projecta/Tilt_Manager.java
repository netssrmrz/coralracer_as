package rs.projecta;

public class Tilt_Manager
implements android.hardware.SensorEventListener
{
  public android.hardware.Sensor mag_sensor, acc_sensor;
	public float[] mag_vals, acc_vals, orig_orient, rot_matrix, orientation,
    diff_orient;
  public android.content.Context ctx;
  public rs.projecta.Tilt_Event_Listener tilt_event_listener;
  
  public Tilt_Manager(android.content.Context ctx)
  {
    this.ctx=ctx;
    this.rot_matrix = new float[9];
    this.orientation = new float[3];
    this.diff_orient=new float[3];
  }
  
  public void Register()
  {
    android.hardware.SensorManager man;

      man = (android.hardware.SensorManager)this.ctx.getSystemService
      (android.content.Context.SENSOR_SERVICE);
      this.mag_sensor = man.getDefaultSensor(
        android.hardware.Sensor.TYPE_MAGNETIC_FIELD);
      this.acc_sensor = man.getDefaultSensor(
        android.hardware.Sensor.TYPE_ACCELEROMETER);
      man.registerListener(this, this.mag_sensor,
        android.hardware.SensorManager.SENSOR_DELAY_GAME);
      man.registerListener(this, this.acc_sensor,
        android.hardware.SensorManager.SENSOR_DELAY_GAME);
	}
  
  public void Unregister()
  {
    android.hardware.SensorManager man;

      man = (android.hardware.SensorManager)this.ctx.getSystemService
      (android.content.Context.SENSOR_SERVICE);
      man.unregisterListener(this);
	}
  
  public void onAccuracyChanged(android.hardware.Sensor p1, int p2)
  {
    // TODO: Implement this method
  }
  
  public void onSensorChanged(android.hardware.SensorEvent e)
  {
    if (e.sensor == this.mag_sensor)
      this.mag_vals = e.values;
    else if (e.sensor == this.acc_sensor)
      this.acc_vals = e.values;

    if (this.mag_vals != null && this.acc_vals != null)
    {
      android.hardware.SensorManager.getRotationMatrix(rot_matrix, null, 
        this.acc_vals, this.mag_vals);
      android.hardware.SensorManager.getOrientation(rot_matrix, orientation);

      if (this.orig_orient==null)
        this.orig_orient=java.util.Arrays.copyOf(orientation, 3);

      if (this.tilt_event_listener!=null)
      {
        this.diff_orient[0]=this.orientation[0]-this.orig_orient[0];
        this.diff_orient[1]=this.orientation[1]-this.orig_orient[1];
        this.diff_orient[2]=this.orientation[2]-this.orig_orient[2];
        
        this.tilt_event_listener.On_Tilt_Changed(
          this.orig_orient, this.orientation, this.diff_orient);
      }
    }
	}
}
