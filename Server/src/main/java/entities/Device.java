package main.java.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Device {
    private String idDevice;
    private Integer userIdUser;
    private User userByUserIdUser;

    @Id
    @Column(name = "idDevice")
    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    @Basic
    @Column(name = "User_idUser")
    public Integer getUserIdUser() {
        return userIdUser;
    }

    public void setUserIdUser(Integer userIdUser) {
        this.userIdUser = userIdUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (idDevice != null ? !idDevice.equals(device.idDevice) : device.idDevice != null) return false;
        if (userIdUser != null ? !userIdUser.equals(device.userIdUser) : device.userIdUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idDevice != null ? idDevice.hashCode() : 0;
        result = 31 * result + (userIdUser != null ? userIdUser.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "User_idUser", referencedColumnName = "idUser", insertable=false, updatable=false)
    public User getUserByUserIdUser() {
        return userByUserIdUser;
    }

    public void setUserByUserIdUser(User userByUserIdUser) {
        this.userByUserIdUser = userByUserIdUser;
    }
}
