package rs.projecta.view;

public class Game2_View
extends android.opengl.GLSurfaceView
implements android.opengl.GLSurfaceView.Renderer
{
  public int col_loc, mat_loc, att_loc;
  public float[] proj;
  public java.util.Stack<float[]> trans_buff;
  public rs.projecta.world.World world;
  public rs.projecta.Debug_Renderer debug_renderer;
  public Object camera;
  public float scale, w, h;

  public Game2_View(android.content.Context ctx, rs.projecta.world.World world)
  {
    super(ctx);

    this.setEGLContextClientVersion(2);
    this.setRenderer(this);
    //this.setRenderMode(android.opengl.GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    this.Init(world);
  }

  public void Init(rs.projecta.world.World w)
  {
    this.world = w;
    this.trans_buff = new java.util.Stack<float[]>();
    this.camera = w.objs.Get_Player();

    if (this.world.debug)
    {
      this.debug_renderer = new rs.projecta.Debug_Renderer(this.world.phys_scale);
      this.world.phys_world.setDebugDraw(this.debug_renderer);
    }
  }

  @Override
  public void onSurfaceCreated(
          javax.microedition.khronos.opengles.GL10 p1,
          javax.microedition.khronos.egl.EGLConfig p2)
  {
    String source;
    int v_shader_id, f_shader_id, prog_id;
    java.nio.FloatBuffer b;

    //android.util.Log.d("onSurfaceCreated()", "Entered");
    android.opengl.GLES20.glClearColor(0, 0, 0, 0.13f);

    source =
            "uniform mat4 u_Matrix;" +
                    "attribute vec4 a_Position; " +
                    "void main(){ gl_Position=u_Matrix*a_Position; }";
    v_shader_id = this.Compile_Shader(android.opengl.GLES20.GL_VERTEX_SHADER, source);

    source =
            "precision mediump float;" +
                    "uniform vec4 u_Color;" +
                    "void main(){ gl_FragColor=u_Color; }";
    f_shader_id = this.Compile_Shader(android.opengl.GLES20.GL_FRAGMENT_SHADER, source);

    if (v_shader_id != 0 && f_shader_id != 0)
    {
      prog_id = this.Link_Program(v_shader_id, f_shader_id);
      // dev only
      this.Validate_Program(prog_id);
      android.opengl.GLES20.glUseProgram(prog_id);

      col_loc = android.opengl.GLES20.glGetUniformLocation(prog_id, "u_Color");
      mat_loc = android.opengl.GLES20.glGetUniformLocation(prog_id, "u_Matrix");
      att_loc = android.opengl.GLES20.glGetAttribLocation(prog_id, "a_Position");

      this.proj = new float[16];
      android.opengl.GLES20.glLineWidth(8);
    }
  }

  @Override
  public void onSurfaceChanged(
          javax.microedition.khronos.opengles.GL10 p1, int w, int h)
  {
    //android.util.Log.d("Game2_View.onSurfaceChanged()", "Entered");
    android.opengl.GLES20.glViewport(0, 0, w, h);
    this.w = w;
    this.h = h;
    this.scale = 0.0003f * (this.w + this.h) + 0.0928f;
    android.opengl.Matrix.orthoM(proj, 0, 0, w, h, 0, -1, 1);
  }

  @Override
  public void onDrawFrame(
          javax.microedition.khronos.opengles.GL10 p1)
  {
    //android.util.Log.d("Game2_View.onDrawFrame()", "Entered");
    this.world.Update();
    this.Draw_World_Step();
  }

  public void Draw_World_Step()
  {
    //android.util.Log.d("Game2_View.Draw_World_Step()", "Entered");

    /*if (this.world.debug)
    {
      this.world.debug_msg[0]="";
      this.world.debug_msg[0]+="c.getWidth(): "+this.w+"\n";
      this.world.debug_msg[0]+="c.getHeight(): "+this.h+"\n";
    }*/

    this.Save_Transform();
    android.opengl.Matrix.translateM(this.proj, 0, this.w / 2f, this.h / 2f, 0);
    android.opengl.Matrix.scaleM(this.proj, 0, this.scale, this.scale, 1f);

    if (this.camera instanceof rs.projecta.object.Has_Direction)
      android.opengl.Matrix.rotateM(
              this.proj, 0,
              -((rs.projecta.object.Has_Direction) this.camera).Get_Angle_Degrees(),
              0, 0, 1);
    if (this.camera instanceof rs.projecta.object.Has_Position)
      android.opengl.Matrix.translateM(
              this.proj, 0,
              -((rs.projecta.object.Has_Position) this.camera).Get_X(),
              -((rs.projecta.object.Has_Position) this.camera).Get_Y(),
              0);

    //c.drawColor(0xff000022);
    android.opengl.GLES20.glClear(android.opengl.GLES20.GL_COLOR_BUFFER_BIT);
    this.world.objs.Draw_OpenGL(this);

    /*if (this.debug_renderer!=null)
    {
      this.debug_renderer.canvas=c;
      this.world.phys_world.drawDebugData();
    }*/

    this.Restore_Transform();

    //if (this.world.debug)
    //this.Draw_Console(c);
  }

  public void Validate_Program(int prog_id)
  {
    int[] status;
    String log;

    android.opengl.GLES20.glValidateProgram(prog_id);

    status = new int[1];
    android.opengl.GLES20.glGetProgramiv(prog_id,
            android.opengl.GLES20.GL_VALIDATE_STATUS, status, 0);
    //if (status[0]==0)
    {
      log = android.opengl.GLES20.glGetProgramInfoLog(prog_id);
      //android.util.Log.d("Validate_Program()", log);
    }
  }

  public int Link_Program(int v_shader_id, int f_shader_id)
  {
    int prog_id = 0;
    int[] status;
    String log;

    prog_id = android.opengl.GLES20.glCreateProgram();
    if (prog_id != 0)
    {
      android.opengl.GLES20.glAttachShader(prog_id, v_shader_id);
      android.opengl.GLES20.glAttachShader(prog_id, f_shader_id);
      android.opengl.GLES20.glLinkProgram(prog_id);

      status = new int[1];
      android.opengl.GLES20.glGetProgramiv(prog_id,
              android.opengl.GLES20.GL_LINK_STATUS, status, 0);
      if (status[0] == 0)
      {
        log = android.opengl.GLES20.glGetProgramInfoLog(prog_id);
        android.opengl.GLES20.glDeleteProgram(prog_id);
        android.util.Log.d("Link_Program()", log);
        prog_id = 0;
      }
    }
    return prog_id;
  }

  public int Compile_Shader(int shader_type, String source)
  {
    int shader_id;
    int[] status;
    String log;

    shader_id = android.opengl.GLES20.glCreateShader(shader_type);
    if (shader_id != 0)
    {
      android.opengl.GLES20.glShaderSource(shader_id, source);
      android.opengl.GLES20.glCompileShader(shader_id);

      status = new int[1];
      android.opengl.GLES20.glGetShaderiv(shader_id,
              android.opengl.GLES20.GL_COMPILE_STATUS, status, 0);
      if (status[0] == 0)
      {
        log = android.opengl.GLES20.glGetShaderInfoLog(shader_id);
        android.opengl.GLES20.glDeleteShader(shader_id);
        android.util.Log.d("Compile_Shader()", log);
        shader_id = 0;
      }
    }
    return shader_id;
  }

  public void Save_Transform()
  {
    this.trans_buff.push(java.util.Arrays.copyOf(this.proj, this.proj.length));
  }

  public void Restore_Transform()
  {
    this.proj = this.trans_buff.pop();
  }

  public void Draw_Obj(float x, float y, float a, rs.projecta.object.Is_Drawable_OpenGL o)
  {
    this.Save_Transform();

    if (x != 0 || y != 0)
      android.opengl.Matrix.translateM(this.proj, 0, x, y, 0);
    if (a != 0)
      android.opengl.Matrix.rotateM(this.proj, 0, a, 0, 0, 1);
    o.Draw_OpenGL(this);

    this.Restore_Transform();
  }

  public void Draw_Obj(rs.projecta.object.Is_Drawable_OpenGL o)
  {
    float x=0, y=0, a=0;

    this.Save_Transform();

    if (o instanceof rs.projecta.object.Has_Position)
    {
      x = ((rs.projecta.object.Has_Position) o).Get_X();
      y = ((rs.projecta.object.Has_Position) o).Get_Y();
      android.opengl.Matrix.translateM(this.proj, 0, x, y, 0);
    }

    if (o instanceof rs.projecta.object.Has_Direction)
    {
      a = ((rs.projecta.object.Has_Direction) o).Get_Angle_Degrees();
      android.opengl.Matrix.rotateM(this.proj, 0, a, 0, 0, 1);
    }

    //this.Draw_Obj(x, y, a, o);
    o.Draw_OpenGL(this);

    this.Restore_Transform();
  }
}
