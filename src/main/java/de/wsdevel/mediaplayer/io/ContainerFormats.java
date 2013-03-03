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

    /** {@link ContainerFormats} The Tele_Typewriter. */
    Tele_Typewriter("Tele-typewriter"),

    /** {@link ContainerFormats} The Sony_OpenMG_audio. */
    Sony_OpenMG_audio("Sony OpenMG audio"),

    /** {@link ContainerFormats} The raw_H_264_video_format. */
    raw_H_264_video_format("raw H.264 video format"),

    /** {@link ContainerFormats} The raw_MPEG_4_video_format. */
    raw_MPEG_4_video_format("raw MPEG-4 video format"),

    /** {@link ContainerFormats} The American_Laser_Games_MM_format. */
    American_Laser_Games_MM_format("American Laser Games MM format"),

    /** {@link ContainerFormats} The Microsoft_Windows_ICO. */
    Microsoft_Windows_ICO("Microsoft Windows ICO"),

    /** {@link ContainerFormats} The PCM_unsigned_24_bit_big_endian_format. */
    PCM_unsigned_24_bit_big_endian_format(
	    "PCM unsigned 24 bit big-endian format");

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