/**
 * Class:    VideoInputStream<br>
 * <br>
 * Created:  03.03.2013<br>
 * Filename: VideoInputStream.java<br>
 * Version:  $Revision: $<br>
 * <br>
 * last modified on $Date: $<br>
 *               by $Author: $<br>
 * <br>
 * @author <a href="http://www.sebastian-weiss.de">Sebastian A. Weiss</a>
 * @version $Author: $ -- $Revision: $ -- $Date: $
 * <br>
 * (c) sweiss 2013 - All rights reserved.
 */
package de.wsdevel.mediaplayer.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.IPacket;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.IVideoResampler;
import com.xuggle.xuggler.Utils;

import de.wsdevel.mediaplayer.Frame;

/**
 * VideoInputStream.
 */
public class VideoInputStream {

    /** {@link IPacket} The packet. */
    private IPacket packet;

    /** {@link long} The timestampInStream. */
    private long timestampInStream;

    /** {@link long} The startTimeTimestamp. */
    private long startTimeTimestamp;

    /** {@link IContainer} The container. */
    private IContainer container;

    /** {@link int} The videoStreamId. */
    private int videoStreamId;

    /** {@link IStreamCoder} The videoCoder. */
    private IStreamCoder videoCoder;

    // /** {@link String} The descriptor. */
    // private String descriptor;

    /** {@link IVideoResampler} The resampler. */
    private IVideoResampler resampler = null;

    /** {@link InputStream} The innerStream. */
    private final InputStream innerStream;

    /** ${Log} the log for this type. */
    private static final Log LOG = LogFactory.getLog(VideoInputStream.class);

    /**
     * VideoInputStream constructor.
     * 
     * @param innerISRef
     *            {@link InputStream}
     * @throws IOException
     * @throws CodecException
     */
    public VideoInputStream(final InputStream innerISRef) throws IOException {
	this.innerStream = innerISRef;

	// Let's make sure that we can actually convert video pixel formats.
	if (!IVideoResampler
		.isSupported(IVideoResampler.Feature.FEATURE_COLORSPACECONVERSION)) {
	    throw new RuntimeException("you must install the GPL version"
		    + " of Xuggler (with IVideoResampler support) for "
		    + "this demo to work");
	}

	Collection<String> include = new LinkedList<String>();
	include.add(ContainerFormats.QuickTime_MPEG4_Motion_JPEG_2000_format
		.getLabel());
	Collection<String> exclude = new LinkedList<String>();
	exclude.add(ContainerFormats.Tele_Typewriter.getLabel());
	exclude.add(ContainerFormats.Sony_OpenMG_audio.getLabel());
	exclude.add(ContainerFormats.American_Laser_Games_MM_format.getLabel());
	exclude.add(ContainerFormats.NC_camera_feed_format.getLabel());
	exclude.add(ContainerFormats.VfW_video_capture.getLabel());
	exclude.add(ContainerFormats.Westwood_Studios_VQA_format.getLabel());
	exclude.add(ContainerFormats.Interplay_C93.getLabel());
	exclude.add(ContainerFormats.Discworld_II_BMV.getLabel());
	exclude.add(ContainerFormats.Tiertex_Limited_SEQ_format.getLabel());
	exclude.add(ContainerFormats.CD_Graphics_Format.getLabel());
	exclude.add(ContainerFormats.Interchange_File_Format.getLabel());
	exclude.add(ContainerFormats.Microsoft_Windows_ICO.getLabel());
	exclude.add(ContainerFormats.PCM_unsigned_24_bit_big_endian_format
		.getLabel());

	this.container = IContainer.make();

	Collection<IContainerFormat> installedInputFormats = IContainerFormat
		.getInstalledInputFormats();
	int index = 0;
	for (IContainerFormat iContainerFormat : installedInputFormats) {
	    System.out.print("[" + index++ + "] "
		    + iContainerFormat.getInputFormatLongName() + "... ");
	    if (exclude.contains(iContainerFormat.getInputFormatLongName())
		    || !include.contains(iContainerFormat
			    .getInputFormatLongName())) {
		System.out.println("EXCLUDED.");
		continue;
	    }
	    // Open up the container
	    // if (this.container
	    // .open("D:/usr/home/sweiss/workspace/nacamar_HBBTV_LSS/run/devel/contents/sintel-1280-surround.mp4",
	    // IContainer.Type.READ, iContainerFormat) >= 0) {
	    if (this.container
		    .open("D:/usr/home/sweiss/workspace/nacamar_HBBTV_LSS/run/devel/contents/sintel_trailer-480p.mp4",
			    IContainer.Type.READ, iContainerFormat) >= 0) {

		// if (this.container.open(this.innerStream, iContainerFormat,
		// true,
		// false) >= 0) {
		final String inputFormatLongName = this.container
			.getContainerFormat().getInputFormatLongName();
		System.out.println("the formats name is ["
			+ inputFormatLongName + "]");
		try {
		    tryToInitFromCurrentContainerFormat();
		    System.out.println("SUCCESS.");
		    break;
		} catch (CodecException ce) {
		    // don't really care, just the wrong container format
		    // (20130308 saw)
		    if (LOG.isDebugEnabled()) {
			LOG.debug(ce.getLocalizedMessage(), ce);
		    }
		    System.out.println("FAILURE.");
		}
	    } else {
		System.out.println("FAILURE.");
	    }
	}

    }

    /**
     * tryToInitFromCurrentContainerFormat.
     * 
     * @throws CodecException
     */
    private void tryToInitFromCurrentContainerFormat() throws CodecException {
	this.videoStreamId = -1;
	this.videoCoder = null;

	// query how many streams the call to open found
	final int numStreams = container.getNumStreams();
	for (int i = 0; i < numStreams; i++) {
	    // Find the stream object
	    final IStream stream = container.getStream(i);
	    // Get the pre-configured decoder that can decode this
	    // stream;
	    final IStreamCoder coder = stream.getStreamCoder();

	    if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
		this.videoStreamId = i;
		this.videoCoder = coder;
		break;
	    }
	}
	if (this.videoStreamId == -1) {
	    throw new CodecException("No video stream in container.");
	}

	/*
	 * Now we have found the video stream in this file. Let's open up our
	 * decoder so it can do work.
	 */
	if (this.videoCoder.open() < 0) {
	    throw new CodecException(
		    "could not open video decoder for container.");
	}

	if (this.videoCoder.getPixelType() != IPixelFormat.Type.BGR24) {
	    // if this stream is not in BGR24, we're going to need
	    // to
	    // convert it. The VideoResampler does that for us.
	    this.resampler = createResampler(this.videoCoder);
	}

	// final Collection<Type> registeredConverters = ConverterFactory
	// .getRegisteredConverters();
	// // SEBASTIAN cleanup this bad code
	// this.descriptor = registeredConverters.iterator().next()
	// .getDescriptor();

	this.timestampInStream = Global.NO_PTS;
	this.startTimeTimestamp = 0;
	this.packet = IPacket.make();
    }

    /**
     * close.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
	this.packet = null;
	if (this.videoCoder != null) {
	    this.videoCoder.close();
	    this.videoCoder = null;
	}
	if (this.container != null) {
	    this.container.close();
	    this.container = null;
	}
	if (this.innerStream != null) {
	    this.innerStream.close();
	}
    }

    /**
     * createResampler.
     * 
     * @return {@link IVideoResampler}
     * @throws CodecException
     */
    public IVideoResampler createResampler(final IStreamCoder streamCoder)
	    throws CodecException {
	final IVideoResampler resampler = IVideoResampler.make(
		streamCoder.getWidth(), streamCoder.getHeight(),
		IPixelFormat.Type.BGR24, streamCoder.getWidth(),
		streamCoder.getHeight(), streamCoder.getPixelType());
	if (resampler == null) {
	    throw new CodecException("could not create color space "
		    + "resampler for.");
	}
	return resampler;
    }

    /**
     * readImage.
     * 
     * @return {@link Frame}
     * @throws CodecException
     */
    public Frame readFrame() throws CodecException {
	while (this.container.readNextPacket(packet) >= 0) {

	    /*
	     * Now we have a packet, let's see if it belongs to our video stream
	     */
	    if (this.packet.getStreamIndex() == this.videoStreamId) {
		/*
		 * We allocate a new picture to get the data out of Xuggler
		 */
		final IVideoPicture picture = IVideoPicture
			.make(this.videoCoder.getPixelType(),
				this.videoCoder.getWidth(),
				this.videoCoder.getHeight());

		int offset = 0;
		while (offset < this.packet.getSize()) {
		    /*
		     * Now, we decode the video, checking for any errors.
		     */
		    final int bytesDecoded = this.videoCoder.decodeVideo(
			    picture, this.packet, offset);
		    if (bytesDecoded < 0) {
			throw new CodecException("Error decoding video.");
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
			if (this.resampler != null) {
			    // we must resample
			    newPic = IVideoPicture.make(
				    this.resampler.getOutputPixelFormat(),
				    picture.getWidth(), picture.getHeight());
			    if (this.resampler.resample(newPic, picture) < 0) {
				throw new CodecException(
					"could not resample video.");
			    }
			}
			if (newPic.getPixelType() != IPixelFormat.Type.BGR24) {
			    throw new CodecException("could not decode video"
				    + " as BGR 24 bit data.");
			}

			// And finally, convert the BGR24 to an Java buffered
			// image
			final BufferedImage javaImage = Utils
				.videoPictureToImage(newPic);

			// final BufferedImage javaImage = ConverterFactory
			// .createConverter(this.descriptor, newPic).toImage(
			// newPic);

			if (this.timestampInStream == Global.NO_PTS) {
			    this.startTimeTimestamp = System
				    .currentTimeMillis();
			}
			this.timestampInStream = picture.getTimeStamp();
			return new Frame(
				javaImage,
				new Date(
					this.startTimeTimestamp
						+ Math.round(this.timestampInStream / 1000d)));
		    }
		}
	    }
	}
	return null;
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: $
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================