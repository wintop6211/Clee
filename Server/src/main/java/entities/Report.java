package main.java.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Report {
    private Integer idReport;
    private String report;
    private Integer userIdUser;
    private User userByUserIdUser;

    @Id
    @Column(name = "idReport")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    @Basic
    @Column(name = "report")
    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
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

        Report report1 = (Report) o;

        if (idReport != null ? !idReport.equals(report1.idReport) : report1.idReport != null) return false;
        if (report != null ? !report.equals(report1.report) : report1.report != null) return false;
        if (userIdUser != null ? !userIdUser.equals(report1.userIdUser) : report1.userIdUser != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idReport != null ? idReport.hashCode() : 0;
        result = 31 * result + (report != null ? report.hashCode() : 0);
        result = 31 * result + (userIdUser != null ? userIdUser.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "User_idUser", referencedColumnName = "idUser", nullable = false, insertable=false, updatable=false)
    public User getUserByUserIdUser() {
        return userByUserIdUser;
    }

    public void setUserByUserIdUser(User userByUserIdUser) {
        this.userByUserIdUser = userByUserIdUser;
    }
}
