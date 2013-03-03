/**
 * Class:    OpenAndPlayCommand<br>
 * <br>
 * Created:  01.03.2013<br>
 * Filename: OpenAndPlayCommand.java<br>
 * Version:  $Revision: $<br>
 * <br>
 * last modified on $Date: $<br>
 *               by $Author: $<br>
 * <br>
 * @author <a href="http://www.sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: $ -- $Revision: $ -- $Date: $
 * <br>
 * (c) Sebastian A. Weiss 2013 - All rights reserved.
 */
package de.wsdevel.mediaplayer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.wsdevel.mediaplayer.Frame;
import de.wsdevel.mediaplayer.Player;
import de.wsdevel.mediaplayer.io.CodecException;
import de.wsdevel.mediaplayer.io.VideoInputStream;
import de.wsdevel.tools.commands.Command;

/**
 * OpenAndPlayCommand.
 */
public class OpenAndPlayCommand implements Command {

    /** {@link Player} The player. */
    private Player player;

    /** {@link File} The toBeOpened. */
    private File toBeOpened;

    /**
     * OpenAndPlayCommand constructor.
     * 
     * @param player
     *            {@link Player}
     * @param toBeOpened
     *            {@link File}
     */
    public OpenAndPlayCommand(final Player player, final File toBeOpened) {
	setPlayer(player);
	setToBeOpened(toBeOpened);
    }

    /**
     * Returns the player.
     * 
     * @return {@link Player}
     */
    public final Player getPlayer() {
	return this.player;
    }

    /**
     * Returns the toBeOpened.
     * 
     * @return {@link File}
     */
    public final File getToBeOpened() {
	return this.toBeOpened;
    }

    /**
     * {@link Log} the logger.
     */
    private static final Log LOG = LogFactory.getLog(OpenAndPlayCommand.class);

    /**
     * @see de.wsdevel.tools.commands.CommandWithThrowable#run()
     */
    public void run() {

	VideoInputStream vis = null;
	try {
	    vis = new VideoInputStream(new FileInputStream(getToBeOpened()));
	    Frame frame = null;
	    boolean initialized = false;
	    while ((frame = vis.readFrame()) != null) {
		if (!initialized) {
		    initialized = true;
		    player.setViewSize(frame.getImage().getWidth(null), frame
			    .getImage().getHeight(null));
		}
		player.scheduleFrame(frame);
	    }
	} catch (FileNotFoundException e) {
	    LOG.error(e.getLocalizedMessage(), e);
	} catch (IOException e) {
	    LOG.error(e.getLocalizedMessage(), e);
	} catch (CodecException e) {
	    LOG.error(e.getLocalizedMessage(), e);
	} finally {
	    if (vis != null) {
		try {
		    vis.close();
		} catch (IOException e) {
		    // don't care
		}
	    }
	}

    }

    /**
     * Sets the player.
     * 
     * @param player
     *            {@link Player}
     */
    public final void setPlayer(final Player player) {
	this.player = player;
    }

    /**
     * Sets the toBeOpened.
     * 
     * @param toBeOpened
     *            {@link File}
     */
    public final void setToBeOpened(final File toBeOpened) {
	this.toBeOpened = toBeOpened;
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: $
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================