package de.java.ejb;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Remote;

import de.java.domain.Drug;
import de.java.ws.MessageDrug;
@Remote
public interface DrugService {

  /**
 * @return all drugs from the database of the HO
 */
Collection<Drug> getAllDrugs();

/**
 * @return all drugs from the JaVa pharmacy database in a HashMap of pzn and MessagDrug
 */
Map<Integer, MessageDrug> getAllDrugsFromJava();

/**
 * @return all drugs from the C Sharpe pharmacy database in a HashMap of pzn and MessagDrug
 */
Map<Integer, MessageDrug> getAllDrugsFromDotNet();

  /**
 * @param searchTerm : name or pzn to be searched for
 * @return all drugs with pzn oder name equal to the seachTerm from the database of the HO
 */
Collection<Drug> getAllDrugsLike(String searchTerm);

  /**
 * @param pzn
 * @return drug with the pzn from the database of the HO
 */
Drug getDrug(int pzn);

  /**
 * @param pzn
 * @return drug with the pzn from the JaVa pharmacy database, accessed via webservices
 */
MessageDrug getDrugFromJava(int pzn);

/**
* @param pzn
* @return drug with the pzn from the C Sharp pharmacy database, accessed via webservices
*/
MessageDrug getDrugFromDotNet(int pzn);

  /**
 * @param drug
 * @return the drug after it is created in the HO database and also in both JaVa and C Sharpe databases via webservices
 */
Drug createDrug(Drug drug);

  /**
 * @param pzn of the drug to be updated
 * @param new name of the drug
 * @param new name of the drug
 * @param new description of the drug
 * @param new drugMinimumAgeYears of the drug
 * @return the drug after it is updated in the HO database and also in both JaVa and C Sharpe databases via webservices
 */
Drug updateMasterData(int pzn, String name, double price, String description, long drugMinimumAgeYears);


/**
 * Runs an initialization script for the main office database. 
 * Only runs, if the HO-database contains no drugs 
 * 1. Drugs from jDrugs and cDrugs are parameters, as they are already fetched in InitDrugs()
 * 2. Pzn based match is performed
 * 3. Is a drug missing on one side, it is created there. If a drug is on both sides, the values
 * 	  of JaVa contain the true data.
 * 4. All drugs are created in the HO-database.
 * @param jDrugs a map of all (already fetched) drugs from JaVa
 * @param cDrugs a map of all (already fetched) drugs from C.Sharpe
 * @return collection of all drugs, that are in the HO-database now
 */
Collection<MessageDrug> initDatabase(Map<Integer, MessageDrug> jDrugs, Map<Integer, MessageDrug> cDrugs);

void removeDrug(int DrugPZN);
}
