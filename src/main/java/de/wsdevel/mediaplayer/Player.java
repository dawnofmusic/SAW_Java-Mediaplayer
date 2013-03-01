/**
 * Class:    Player<br>
 * <br>
 * Created:  22.02.2013<br>
 * Filename: Player.java<br>
 * Version:  $Revision: 1.1 $<br>
 * <br>
 * last modified on $Date: 2013-02-22 20:02:08 $<br>
 *               by $Author: sweiss $<br>
 * <br>
 * @author <a href="http://www.sebastian-weiss.de">Sebastian A. Weiss GmbH</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2013-02-22 20:02:08 $
 * <br>
 * (c) Sebastian A. Weiss, 2013 - All rights reserved.
 */
package de.wsdevel.mediaplayer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Player.
 */
public class Player extends JFrame {

    /**
     * {@link Logger} the LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Player.class);

    /** {@link long} The serialVersionUID. */
    private static final long serialVersionUID = 7345126953094136165L;

    /**
     * {@code boolean true} if frame buffer is full;
     */
    private boolean frameBufferFull = false;

    /** {@link ConcurrentLinkedQueue<Frame>} The frameQueue. */
    private final ConcurrentLinkedQueue<Frame> frameQueue = new ConcurrentLinkedQueue<Frame>();

    /**
     * {@code int} the maximum number of frames to be buffered;
     */
    private int maxFramesBufferSize = 100;

    /**
     * {@link PropertyChangeSupport}
     */
    private final PropertyChangeSupport pcs;

    /**
     * {@link Timer} used for showing frames.
     */
    private final Timer playerTimer = new Timer("Player"); //$NON-NLS-1$

    /**
     * {@code boolean} determining whether player is in play mode or not.
     */
    private boolean playing = false;

    /** {@link VideoView} The view. */
    private final VideoView view;

    /**
     * Default constructor.
     */
    public Player() {
	super(".: SAW Java Mediaplayer :."); //$NON-NLS-1$
	this.pcs = new PropertyChangeSupport(this);
	this.view = new VideoView();
	setContentPane(this.view);
	this.view.addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentResized(final ComponentEvent e) {
		pack();
	    }
	});

	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(final WindowEvent e) {
		System.exit(0);
	    }
	});

	setVisible(true);
    }

    /**
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
	this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            {@link String}
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(final String propertyName,
	    final PropertyChangeListener listener) {
	this.pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @return the maxFramesBufferSize
     */
    public int getMaxFramesBufferSize() {
	return this.maxFramesBufferSize;
    }

    /**
     * @return the frameBufferFull
     */
    public boolean isFrameBufferFull() {
	return this.frameBufferFull;
    }

    /**
     * @return the playing
     */
    public boolean isPlaying() {
	return this.playing;
    }

    /**
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(
	    final PropertyChangeListener listener) {
	this.pcs.removePropertyChangeListener(listener);
    }

    /**
     * @param propertyName
     *            {@link String}
     * @param listener
     *            {@link PropertyChangeListener}
     * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String,
     *      java.beans.PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(final String propertyName,
	    final PropertyChangeListener listener) {
	this.pcs.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * scheduleFrame.
     * 
     * @param frame
     *            {@link Image}
     * @param delay
     *            {@code long}
     */
    public void scheduleFrame(final Frame frame) {
	this.frameQueue.offer(frame);
	if (this.frameQueue.size() >= getMaxFramesBufferSize()) {
	    setFrameBufferFull(true);
	}
	this.playerTimer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		try {
		    final Frame poll = Player.this.frameQueue.poll();
		    if (isFrameBufferFull()
			    && (Player.this.frameQueue.size() < (getMaxFramesBufferSize() * 0.8))) {
			setFrameBufferFull(false);
		    }
		    if (poll != null) {
			Player.this.view.setFrame(poll.getImage());
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
				Player.this.view.repaint();
				setPlaying(true);
			    }
			});
		    } else {
			SwingUtilities.invokeAndWait(new Runnable() {
			    public void run() {
				setPlaying(false);
			    }
			});
		    }
		} catch (final Throwable t) {
		    Player.LOG.error(t.getLocalizedMessage(), t);
		}
	    }
	}, frame.getTimestamp());
    }

    /**
     * @param frameBufferFullVal
     *            the frameBufferFull to set
     */
    private void setFrameBufferFull(final boolean frameBufferFullVal) {
	final boolean oldValue = this.frameBufferFull;
	this.frameBufferFull = frameBufferFullVal;
	if (this.frameBufferFull != oldValue) {
	    this.pcs.firePropertyChange("frameBufferFull", oldValue, //$NON-NLS-1$
		    this.frameBufferFull);
	}
    }

    /**
     * @param maxFramesBufferSizeVal
     *            the maxFramesBufferSize to set
     */
    public void setMaxFramesBufferSize(final int maxFramesBufferSizeVal) {
	final int oldValue = this.maxFramesBufferSize;
	this.maxFramesBufferSize = maxFramesBufferSizeVal;
	this.pcs.firePropertyChange(
		"maxFramesBufferSize", oldValue, this.maxFramesBufferSize); //$NON-NLS-1$
    }

    /**
     * @param playingVal
     *            the playing to set
     */
    private void setPlaying(final boolean playingVal) {
	final boolean oldVal = this.playing;
	this.playing = playingVal;
	if (this.playing != oldVal) {
	    this.pcs.firePropertyChange("playing", oldVal, this.playing); //$NON-NLS-1$
	}
    }

    /**
     * @param width
     *            {@code int}
     * @param height
     *            {@code int}
     */
    public void setViewSize(final int width, final int height) {
	if (this.view != null) {
	    final Dimension size = new Dimension(width, height);
	    this.view.setPreferredSize(size);
	    this.view.setSize(size);
	}
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: Player.java,v $
// Revision 1.1 2013-02-22 20:02:08 sweiss
// first steps implementing a player
//
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================