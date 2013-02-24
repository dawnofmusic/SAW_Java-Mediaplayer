/**
 * Class:    ImagePanel<br>
 * <br>
 * Created:  22.02.2013<br>
 * Filename: ImagePanel.java<br>
 * Version:  $Revision: 1.1 $<br>
 * <br>
 * last modified on $Date: 2013-02-22 20:02:08 $<br>
 *               by $Author: sweiss $<br>
 * <br>
 * @author <a href="http://www.sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: sweiss $ -- $Revision: 1.1 $ -- $Date: 2013-02-22 20:02:08 $
 * <br>
 * (c) Sebastian A. Weiss, 2013 - All rights reserved.
 */
package de.wsdevel.mediaplayer;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * ImagePanel.
 */
public class VideoView extends JComponent {

    /** {@link long} The serialVersionUID. */
    private static final long serialVersionUID = 356677462215058852L;

    /** {@link Image} The image. */
    private Image frame;

    /**
     * ImagePanel constructor.
     */
    public VideoView() {
	super();
	setLayout(null);
    }

    /**
     * Returns the frame.
     * 
     * @return {@link Image}
     */
    public final Image getFrame() {
	return this.frame;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     * @param g
     *            {@link Graphics}
     */
    @Override
    protected void paintComponent(final Graphics g) {
	if (this.frame != null) {
	    g.drawImage(this.frame, 0, 0, getWidth(), getHeight(), this);
	}
    }

    /**
     * Sets the frame.
     * 
     * @param frameRef
     *            {@link Image}
     */
    public final void setFrame(final Image frameRef) {
	this.frame = frameRef;
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: VideoView.java,v $
// Revision 1.1 2013-02-22 20:02:08 sweiss
// first steps implementing a player
//
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================