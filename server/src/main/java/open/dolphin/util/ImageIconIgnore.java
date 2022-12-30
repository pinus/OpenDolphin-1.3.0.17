package open.dolphin.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.accessibility.AccessibleContext;

/**
 * prevent java.desktop to see private field AccessibleImageIcon in javax.swing.
 *
 * @author pns
 */
public interface ImageIconIgnore {
    @JsonIgnore
    AccessibleContext getAccessibleContext();
}
