/**
 * 
 */
package vialab.simpleMultiTouch.events;

/**
 * @author Mark Hancock
 *
 */
public interface MultiTouchEventListener {
	
	void touchDown(MultiTouchEvent e);
	
	void touchUp(MultiTouchEvent e);
	
	void touchMoved(MultiTouchEvent e);
}
