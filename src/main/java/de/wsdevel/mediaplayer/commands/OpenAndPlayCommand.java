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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Date;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.ConverterFactory.Type;

import de.wsdevel.mediaplayer.Frame;
import de.wsdevel.mediaplayer.Player;
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
     * @see de.wsdevel.tools.commands.CommandWithThrowable#run()
     */
    public void run() {

	final String absolutePath = getToBeOpened().getAbsolutePath();

	// Let's make sure that we can actually convert video pixel formats.
	if (!IVideoResampler
		.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) {
	    throw new RuntimeException("you must install the GPL version"
		    + " of Xuggler (with IVideoResampler support) for "
		    + "this demo to work");
	}

	// Create a Xuggler container object
	IContainer container = IContainer.make();

	// Open up the container
	if (container.open(absolutePath, IContainer.Type.READ, null) < 0) {
	    throw new IllegalArgumentException("could not open file: "
		    + absolutePath);
	}

	// query how many streams the call to open found
	final int numStreams = container.getNumStreams();

	// and iterate through the streams to find the first video stream
	int videoStreamId = -1;
	IStreamCoder videoCoder = null;
	for (int i = 0; i < numStreams; i++) {
	    // Find the stream object
	    final IStream stream = container.getStream(i);
	    // Get the pre-configured decoder that can decode this stream;
	    final IStreamCoder coder = stream.getStreamCoder();

	    if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
		videoStreamId = i;
		videoCoder = coder;
		break;
	    }
	}
	if (videoStreamId == -1) {
	    throw new RuntimeException(
		    "could not find video stream in container: " + absolutePath);
	}

	/*
	 * Now we have found the video stream in this file. Let's open up our
	 * decoder so it can do work.
	 */
	if (videoCoder.open() < 0) {
	    throw new RuntimeException(
		    "could not open video decoder for container: "
			    + absolutePath);
	}

	IVideoResampler resampler = null;
	if (videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
	    // if this stream is not in BGR24, we're going to need to
	    // convert it. The VideoResampler does that for us.
	    resampler = IVideoResampler.make(videoCoder.getWidth(),
		    videoCoder.getHeight(), IPixelFormat.Type.BGR24,
		    videoCoder.getWidth(), videoCoder.getHeight(),
		    videoCoder.getPixelType());
	    if (resampler == null) {
		throw new RuntimeException("could not create color space "
			+ "resampler for: " + absolutePath);
	    }
	}

	final Collection<Type> registeredConverters = ConverterFactory
		.getRegisteredConverters();
	final String descriptor = registeredConverters.iterator().next()
		.getDescriptor();

	long timestampInStream = Global.NO_PTS;
	long startTimeTimestamp = 0;

	/*
	 * Now, we start walking through the container looking at each packet.
	 */
	final IPacket packet = IPacket.make();
	while (container.readNextPacket(packet) >= 0) {
	    /*
	     * Now we have a packet, let's see if it belongs to our video stream
	     */
	    if (packet.getStreamIndex() == videoStreamId) {
		/*
		 * We allocate a new picture to get the data out of Xuggler
		 */
		final IVideoPicture picture = IVideoPicture.make(
			videoCoder.getPixelType(), videoCoder.getWidth(),
			videoCoder.getHeight());

		int offset = 0;
		while (offset < packet.getSize()) {
		    /*
		     * Now, we decode the video, checking for any errors.
		     */
		    final int bytesDecoded = videoCoder.decodeVideo(picture,
			    packet, offset);
		    if (bytesDecoded < 0) {
			throw new RuntimeException(
				"got error decoding video in: " + absolutePath);
		    }
		    offset += bytesDecoded;

		    /*
		     * Some decoders will consume data in a packet, but will not
		     * be able to construct a full video picture yet. Therefore
		     * you should always check if you got a complete picture
		     * from the decoder
		     */
		    if (picture.isComplete()) {
			IVideoPicture newPic = picture;
			/*
			 * If the resampler is not null, that means we didn't
			 * get the video in BGR24 format and need to convert it
			 * into BGR24 format.
			 */
			if (resampler != null) {
			    // we must resample
			    newPic = IVideoPicture.make(
				    resampler.getOutputPixelFormat(),
				    picture.getWidth(), picture.getHeight());
			    if (resampler.resample(newPic, picture) < 0) {
				throw new RuntimeException(
					"could not resample video from: "
						+ absolutePath);
			    }
			}
			if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
			    throw new RuntimeException("could not decode video"
				    + " as BGR 24 bit data in: " + absolutePath);
			}

			// And finally, convert the BGR24 to an Java buffered
			// image
			// final BufferedImage javaImage = Utils
			// .videoPictureToImage(newPic);

			final BufferedImage javaImage = ConverterFactory
				.createConverter(descriptor, newPic).toImage(
					newPic);

			if (timestampInStream == Global.NO_PTS) {
			    startTimeTimestamp = System.currentTimeMillis();
			    player.setViewSize(javaImage.getWidth(),
				    javaImage.getHeight());
			}
			timestampInStream = picture.getTimeStamp();

			player.scheduleFrame(new Frame(
				javaImage,
				new Date(startTimeTimestamp
					+ Math.round(timestampInStream / 1000d))));
		    }
		}
	    }

	}
	/*
	 * Technically since we're exiting anyway, these will be cleaned up by
	 * the garbage collector... but because we're nice people and want to be
	 * invited places for Christmas, we're going to show how to clean up.
	 */
	if (videoCoder != null) {
	    videoCoder.close();
	    videoCoder = null;
	}
	if (container != null) {
	    container.close();
	    container = null;
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