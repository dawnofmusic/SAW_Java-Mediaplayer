/**
 * Class:    ContainerFormats<br>
 * <br>
 * Created:  04.03.2013<br>
 * Filename: ContainerFormats.java<br>
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

/**
 * ContainerFormats.
 */
public enum ContainerFormats {

    /** {@link ContainerFormats} The American_Laser_Games_MM_format. */
    American_Laser_Games_MM_format("American Laser Games MM format"),

    /** {@link ContainerFormats} The Apple_HTTP_Live_Streaming_format. */
    Apple_HTTP_Live_Streaming_format("Apple HTTP Live Streaming format"),

    /** {@link ContainerFormats} The CD_Graphics_Format. */
    CD_Graphics_Format("CD Graphics Format"),

    /** {@link ContainerFormats} The Discworld_II_BMV. */
    Discworld_II_BMV("Discworld II BMV"),

    /** {@link ContainerFormats} The Interchange_File_Format. */
    Interchange_File_Format("Interchange File Format"),

    /** {@link ContainerFormats} The Interplay_C93. */
    Interplay_C93("Interplay C93"),

    /** {@link ContainerFormats} The Microsoft_Windows_ICO. */
    Microsoft_Windows_ICO("Microsoft Windows ICO"),

    /** {@link ContainerFormats} The NC_camera_feed_format. */
    NC_camera_feed_format("NC camera feed format"),

    /** {@link ContainerFormats} The PCM_unsigned_24_bit_big_endian_format. */
    PCM_unsigned_24_bit_big_endian_format(
	    "PCM unsigned 24 bit big-endian format"),

    /** {@link ContainerFormats} The QuickTime_MPEG4_Motion_JPEG_2000_format. */
    QuickTime_MPEG4_Motion_JPEG_2000_format(
	    "QuickTime/MPEG-4/Motion JPEG 2000 format"),

    /** {@link ContainerFormats} The raw_H_264_video_format. */
    raw_H_264_video_format("raw H.264 video format"),

    /** {@link ContainerFormats} The raw_MPEG_4_video_format. */
    raw_MPEG_4_video_format("raw MPEG-4 video format"),

    /** {@link ContainerFormats} The Sony_OpenMG_audio. */
    Sony_OpenMG_audio("Sony OpenMG audio"),

    /** {@link ContainerFormats} The Tele_Typewriter. */
    Tele_Typewriter("Tele-typewriter"),

    /** {@link ContainerFormats} The Tiertex_Limited_SEQ_format. */
    Tiertex_Limited_SEQ_format("Tiertex Limited SEQ format"),

    // SEBASTIAN works with webcam!
    /** {@link ContainerFormats} The VfW_video_capture. */
    VfW_video_capture("VfW video capture"),

    /** {@link ContainerFormats} The Westwood Studios VQA format. */
    Westwood_Studios_VQA_format("Westwood Studios VQA format"),

    // SEBASTIAN works with webcam?
    /** {@link ContainerFormats} The Windows_Television_WTV. */
    Windows_Television_WTV("Windows Television (WTV)");

    /** {@link String} The label. */
    private String label;

    /**
     * ContainerFormats constructor.
     * 
     * @param labelVal
     *            {@link String}
     */
    private ContainerFormats(final String labelVal) {
	this.label = labelVal;
    }

    /**
     * Returns the label.
     * 
     * @return {@link String}
     */
    public String getLabel() {
	return this.label;
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: $
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================