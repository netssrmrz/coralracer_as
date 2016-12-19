package rs.projecta;

public class Camera
{
	public float pos_x, pos_y;
	public float size_x, size_y;
	public float dir_angle;
	
	public Camera()
	{
		this.pos_x=0;
		this.pos_y=0;
		this.size_x=0;
		this.dir_angle=0;
	}
	
	public void Move_To(android.graphics.Canvas c, float pos_x, float pos_y, 
	  float dir_angle)
	{
		this.pos_x=pos_x;
		this.pos_y=pos_y;
		this.dir_angle=dir_angle;
		
		c.translate(-this.Get_Left(), -this.Get_Top());
		c.rotate(-this.dir_angle, this.pos_x, this.pos_y);
	}
	
	public float Get_Left()
	{
		return this.pos_x-(this.size_x/(float)2);
	}
	
	public float Get_Top()
	{
		return this.pos_y-(this.size_y/(float)2);
	}
	
	public float Get_Right()
	{
		return this.pos_x+(this.size_x/(float)2);
	}
	
	public float Get_Bottom()
	{
		return this.pos_y+(this.size_y/(float)2);
	}
}
