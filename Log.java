/*
 * Created on 2003/07/13
 * $Id: Log.java,v 1.1 2004/01/18 18:02:52 matsu Exp $
 *
 */
/**
 * コンソール出力
 * Log4Jみたいなことをやってる
 * 
 * @author Yuki Matsukura <matsu@ht.sfc.keio.ac.jp>
 */
public class Log {


  private static final int DEBUG = 0;
  private static final int INFO  = 1;
  private static final int WARN  = 2;
  private static final int FATAL = 3;

  /** Log output level */
  private static int mode = 1;


  public static void debug(Object message) {
    if(mode == 0 )
      System.out.println("DEBUG: "+message);
  }

  public static void info(Object message) {
    if(mode <= 1)
      System.out.println("INFO: "+message);
  }

  public static void warn(Object message) {
    if(mode <= 2)
      System.out.println("WARN: "+message);
  }

  public static void fatal(Object message) {
    if(mode <= 3)
      System.out.println("FATAL: "+message);
  }
}
