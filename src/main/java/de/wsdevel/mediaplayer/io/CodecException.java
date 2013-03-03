/**
 * Class:    CodecException<br>
 * <br>
 * Created:  03.03.2013<br>
 * Filename: CodecException.java<br>
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
 * CodecException.
 */
public class CodecException extends Exception {

    /** {@link long} The serialVersionUID. */
    private static final long serialVersionUID = 7346536169662325090L;

    /**
     * CodecException constructor.
     */
    public CodecException() {
    }

    /**
     * CodecException constructor.
     * 
     * @param message
     */
    public CodecException(final String message) {
	super(message);
    }

    /**
     * CodecException constructor.
     * 
     * @param message
     * @param cause
     */
    public CodecException(final String message, final Throwable cause) {
	super(message, cause);
    }

    /**
     * CodecException constructor.
     * 
     * @param cause
     */
    public CodecException(final Throwable cause) {
	super(cause);
    }

}

// ==============[VERSION-CONTROL-LOG-START]==============
// -------------------------------------------------------
// $Log: $
// _______________________________________________________
// ==============[VERSION-CONTROL-LOG-END]================