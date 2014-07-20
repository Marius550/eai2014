package de.java.web;

import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import de.java.domain.Drug;
import de.java.ejb.DrugService;

@ManagedBean
public class DrugList implements Serializable {
  private static final long serialVersionUID = 9000977856559982072L;

  @EJB
  private DrugService drugService;

  private String searchTerm;

  public Collection<Drug> getDrugs() {
    return drugService.getAllDrugsLike(searchTerm);
  }

  public String getSearchTerm() {
    return searchTerm;
  }

  public void setSearchTerm(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  public String search() {
    return "/drug/list.xhtml?faces-redirect=true&search=" + searchTerm;
  }

}
