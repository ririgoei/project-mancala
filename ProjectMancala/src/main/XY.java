package main;
/**
 * <marquee>I <b>&lt;3</b> Eclipse.</marquee>
 * @author professor Vaganov
 * @version $Id: XY.java 15 2011-02-08 05:27:47Z RujinXERO $
 *
 */
public class XY {
	public int x, y;
	public XY(int a_x, int a_y)
	{
		setX(a_x);
		setY(a_y);
	}
	public XY()
	{
		setX(0);
		setY(0);
	}
	public void set(int a_x, int a_y)
	{
		setX(a_x);
		setY(a_y);
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
}
