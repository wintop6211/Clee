package main.java.entities.managements;

import main.java.entities.Device;
import org.hibernate.Session;

public class DeviceManagement {

    final Session session;

    public DeviceManagement(Session session) {
        this.session = session;
    }

    public void add(Device device) {
        session.save(device);
    }

    public Device get(String idDevice) {
        return session.load(Device.class, idDevice);
    }

    public void set(Device device) {
        session.update(device);
    }

    public boolean isExist(Device device) {
        boolean isExist = false;
        if (device.getIdDevice() != null) {
            device = session.get(Device.class, device.getIdDevice());
            isExist = device != null;
        }
        return isExist;
    }
}
