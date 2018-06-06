package main.java.entities.managements;

import main.java.entities.Device;
import org.hibernate.Session;

/**
 * The class is for managing all devices stored in the database.
 */
public class DeviceManagement {

    final Session session;

    /**
     * Creates the device manager
     *
     * @param session The session for interacting with the database
     */
    public DeviceManagement(Session session) {
        this.session = session;
    }

    /**
     * Adds the device to the database
     *
     * @param device The device which needs to be added
     */
    public void add(Device device) {
        session.save(device);
    }

    /**
     * Get the device by using the
     *
     * @param idDevice The id of the device
     * @return The reference which refers to the row of the table
     */
    public Device get(String idDevice) {
        return session.load(Device.class, idDevice);
    }

    /**
     * Update the existed device in the database
     *
     * @param device The device with the updated information
     */
    public void set(Device device) {
        session.update(device);
    }

    /**
     * Determine if the device has existed in the database or not
     *
     * @param device The device object
     * @return True if the device has existed; false if the device does not exist
     */
    public boolean isExist(Device device) {
        boolean isExist = false;
        if (device.getIdDevice() != null) {
            device = session.get(Device.class, device.getIdDevice());
            isExist = device != null;
        }
        return isExist;
    }
}
