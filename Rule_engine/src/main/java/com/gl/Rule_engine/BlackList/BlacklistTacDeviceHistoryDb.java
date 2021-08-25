/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine.BlackList;

import java.io.Serializable;


class BlacklistTacDeviceHistoryDb implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)/
    private int id;

//    @Column
    private String DateReported;
    private String Organisation;
    private String OrganisationType;
    private String Country;
    private String Action;
    private String Reason;

//    @ManyToOne
//    @JoinColumn(name = "blacklist_tac _db_id")
    private BlacklistTacDb blacklistTacDb;

    public BlacklistTacDb getBlacklistTacDb() {
        return blacklistTacDb;
    }

    public void setBlacklistTacDb(BlacklistTacDb blacklistTacDb) {
        this.blacklistTacDb = blacklistTacDb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateReported() {
        return DateReported;
    }

    public void setDateReported(String DateReported) {
        this.DateReported = DateReported;
    }

    public String getOrganisation() {
        return Organisation;
    }

    public void setOrganisation(String Organisation) {
        this.Organisation = Organisation;
    }

    public String getOrganisationType() {
        return OrganisationType;
    }

    public void setOrganisationType(String OrganisationType) {
        this.OrganisationType = OrganisationType;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

   

    BlacklistTacDeviceHistoryDb() {
    }

    @Override
    public String toString() {
        return "BlacklistTacDeviceHistoryDb{" + "id=" + id + ", DateReported=" + DateReported + ", Organisation=" + Organisation + ", OrganisationType=" + OrganisationType + ", Country=" + Country + ", Action=" + Action + ", Reason=" + Reason + ", blacklistTacDb=" + blacklistTacDb + '}';
    }

    public BlacklistTacDeviceHistoryDb(int id, String DateReported, String Organisation, String OrganisationType, String Country, String Action, String Reason, BlacklistTacDb blacklistTacDb) {
        this.id = id;
        this.DateReported = DateReported;
        this.Organisation = Organisation;
        this.OrganisationType = OrganisationType;
        this.Country = Country;
        this.Action = Action;
        this.Reason = Reason;
        this.blacklistTacDb = blacklistTacDb;
    }
    
    

}
