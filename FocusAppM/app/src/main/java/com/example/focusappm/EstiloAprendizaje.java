package com.example.focusappm;

import java.io.Serializable;

public class EstiloAprendizaje implements Serializable {
    private Integer ec;
    private Integer or;
    private Integer ca;
    private Integer ea;
    private String idBeneficiario;
    private String idEstilo;
    private String dominate;
    private String secundario;

    public EstiloAprendizaje( ) {

    }

    public Integer getEc() {
        return ec;
    }

    public void setEc(Integer ec) {
        this.ec = ec;
    }

    public Integer getOr() {
        return or;
    }

    public void setOr(Integer or) {
        this.or = or;
    }

    public Integer getCa() {
        return ca;
    }

    public void setCa(Integer ca) {
        this.ca = ca;
    }

    public Integer getEa() {
        return ea;
    }

    public void setEa(Integer ea) {
        this.ea = ea;
    }

    public String getIdBeneficiario() {
        return idBeneficiario;
    }

    public void setIdBeneficiario(String idBeneficiario) {
        this.idBeneficiario = idBeneficiario;
    }

    public String getDominate() {
        return dominate;
    }

    public void setDominate(String dominate) {
        this.dominate = dominate;
    }

    public String getSecundario() {
        return secundario;
    }

    public void setSecundario(String secundario) {
        this.secundario = secundario;
    }

    public String getIdEstilo() {
        return idEstilo;
    }

    public void setIdEstilo(String idEstilo) {
        this.idEstilo = idEstilo;
    }
}
