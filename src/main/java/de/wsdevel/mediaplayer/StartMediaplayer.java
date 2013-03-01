/**
 * Class:    StartMediaplayer<br>
 * <br>
 * Created:  01.03.2013<br>
 * Filename: StartMediaplayer.java<br>
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
package de.wsdevel.mediaplayer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import org.apache.log4j.BasicConfigurator;

import de.wsdevel.mediaplayer.commands.OpenAndPlayCommand;

/**
 * StartMediaplayer.
 */
public class StartMediaplayer {

    /**
     * main.
     * 
     * @param args
     *            {@link String}[]
     */
    public static void main(String[] args) {
	BasicConfigurator.configure();
	final Player player = new Player();
	player.addPropertyChangeListener(
		"frameBufferFull", new PropertyChangeListener() { //$NON-NLS-1$
		    public void propertyChange(PropertyChangeEvent evt) {
			if ((Boolean) evt.getNewValue()) {
			    while (player.isFrameBufferFull()) {
				try {
				    Thread.sleep(1000);
				} catch (InterruptedException e) {
				    // don't care
				}
			    }
			}
		    }
		});

	new OpenAndPlayCommand(player, new File(
		"run/devel/contents/sintel_trailer-480p.mp4")).run();
    }

}
