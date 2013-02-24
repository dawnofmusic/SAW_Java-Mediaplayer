/**
 * Class:    Frame<br>
 * <br>
 * Created:  22.02.2013<br>
 * Filename: Frame.java<br>
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

import java.awt.Image;
import java.util.Date;

/**
 * Frame.
 */
public class Frame {

    /** {@link Date} The timestamp. */
    private Date timestamp;

    /** {@link Image} The image. */
    private Image image;

    /**
     * Frame constructor.
     */
    public Frame() {
    }

    /**
     * Frame constructor.
     * 
     * @param imageRef
     *            {@link Image}
     * @param timestampVal
     *            {@code Date}
     */
    public Frame(final Image imageRef, final Date timestampVal) {
	setImage(imageRef);
	setTimestamp(timestampVal);
    }

    /**
     * Returns the image.
     * 
     * @return {@link Image}
     */
    public final Image getImage() {
	return this.image;
    }

    /**
     * Returns the delay.
     * 
     * @return {@link Date}
     */
    public final Date getTimestamp() {
	return this.timestamp;
    }

    /**
     * Sets the image.
     * 
     * @param imageRef
     *            {@link Image}
     */
    public final void setImage(final Image imageRef) {
	this.image = imageRef;
    }

    /**
     * Sets the delay.
     * 
     * @param timestampVal
     *            {@link long}
     */
    public final void setTimestamp(final Date timestampVal) {
	this.timestamp = timestampVal;
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: Frame.java,v $
// Revision 1.1 2013-02-22 20:02:08 sweiss
// first steps implementing a player
//
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================